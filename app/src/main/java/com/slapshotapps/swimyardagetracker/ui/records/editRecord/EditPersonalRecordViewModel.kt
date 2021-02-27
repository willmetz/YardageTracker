package com.slapshotapps.swimyardagetracker.ui.records.editRecord

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.slapshotapps.swimyardagetracker.R
import com.slapshotapps.swimyardagetracker.models.personalrecords.RecordTime
import com.slapshotapps.swimyardagetracker.models.personalrecords.YardageTrackerPersonalRecord
import com.slapshotapps.swimyardagetracker.models.personalrecords.YardageTrackerRecordTime
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutUoM
import com.slapshotapps.swimyardagetracker.repositories.PersonalRecordsRepository
import com.slapshotapps.swimyardagetracker.utils.StringProvider
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

sealed class EditPersonalRecordEvent {
    object Ready : EditPersonalRecordEvent()
    data class DeleteRecordError(val msg: String) : EditPersonalRecordEvent()
    data class InvalidTimeError(val msg: String, val uom: WorkoutUoM) : EditPersonalRecordEvent()
    data class InvalidDate(val msg: String, val uom: WorkoutUoM) : EditPersonalRecordEvent()
    data class Success(val msg: String) : EditPersonalRecordEvent()
    data class UpdateRecordDisplayDate(val newDate: String, val uom: WorkoutUoM) : EditPersonalRecordEvent()
    object Loading : EditPersonalRecordEvent()
}

class EditPersonalRecordViewModel @Inject constructor(
    private val personalRecordsRepository: PersonalRecordsRepository,
    private val stringProvider: StringProvider
) {
    private val dateFormatForRecord = SimpleDateFormat("MMMM d yyyy", Locale.US)
    private var recordID = 0L

    private var personalRecord: YardageTrackerPersonalRecord ? = null
    private val viewModelMutableEvent: MutableLiveData<EditPersonalRecordEvent> = MutableLiveData(EditPersonalRecordEvent.Loading)
    val viewModelEvent: LiveData<EditPersonalRecordEvent>
        get() = viewModelMutableEvent

    private val personalRecordData: LiveData<EditableRecordModel> =
        Transformations.map(personalRecordsRepository.getRecord(recordID)) { existingRecord ->

            personalRecord = existingRecord

            viewModelMutableEvent.value = EditPersonalRecordEvent.Ready

            createViewDataForRecord(existingRecord)
    }

    fun getRecord(recordId: Long): LiveData<EditableRecordModel> {
        this.recordID = recordId
        return personalRecordData
    }

    private fun createViewDataForRecord(record: YardageTrackerPersonalRecord): EditableRecordModel {
        val eventName = "${record.distance} ${record.stroke}"

        val yardageRecord = record.recordTimes.firstOrNull { record -> record.uom == WorkoutUoM.YARDS }
        val metricRecord = record.recordTimes.firstOrNull { record -> record.uom == WorkoutUoM.METERS }

        return EditableRecordModel(eventName, buildTimeRecordModel(yardageRecord, WorkoutUoM.YARDS),
            buildTimeRecordModel(metricRecord, WorkoutUoM.METERS))
    }

    private fun buildTimeRecordModel(time: YardageTrackerRecordTime?, uom: WorkoutUoM): EditableTimeRecordModel {
        if (time == null) {
            return EditableTimeRecordModel(false, "", "", "", Date(), "")
        }
        val minutes = String.format("%d", time.minutes)
        val seconds = String.format("%02d", time.seconds)
        val milliseconds = String.format("%02d", time.milliseconds)

        val formattedDate = dateFormatForRecord.format(time.date ?: Date())

        return EditableTimeRecordModel(true, minutes, seconds, milliseconds, time.date ?: Date(), formattedDate)
    }

    fun getDateFromString(dateStr: String?): Date? {
        return if (dateStr.isNullOrEmpty()) null else dateFormatForRecord.parse(dateStr)
    }

    fun updateDate(year: Int, month: Int, dayOfMonth: Int, uom: WorkoutUoM) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)

        viewModelMutableEvent.value = EditPersonalRecordEvent.UpdateRecordDisplayDate(dateFormatForRecord.format(calendar.time), uom)
    }

    suspend fun updateRecord(updatedRecords: List<YardageTrackerRecordTime>) {
        if (updatedRecords.isEmpty()) {
            viewModelMutableEvent.value =
                EditPersonalRecordEvent.DeleteRecordError(stringProvider.getString(R.string.delete_unavailable))
            return
        }

        // validate update
        for (record in updatedRecords) {
            // check that time is valid
            if (record.minutes <= 0 && record.seconds <= 0 && record.milliseconds <= 0) {
                viewModelMutableEvent.value =
                    EditPersonalRecordEvent.InvalidTimeError("Time is required", record.uom)
                return
            }
            if (record.date == null) {
                viewModelMutableEvent.value = EditPersonalRecordEvent.InvalidDate("Date is required", record.uom)
                return
            }
        }

        // create the new record based on the original record
        val currentRecord = personalRecord!!
        val newRecordTimes = ArrayList<RecordTime>()
        for (updatedRecord in updatedRecords) {
            val timeId = currentRecord.recordTimes.firstOrNull { it.uom == updatedRecord.uom }?.id ?: 0
            val time = updatedRecord.toRecordTime(timeId)
            newRecordTimes.add(time!!)
        }

        if (currentRecord.recordTimes.count() > newRecordTimes.count()) {
            // a record needs to be deleted
            for (existingRecord in currentRecord.recordTimes) {
                if (newRecordTimes.firstOrNull { it.unitOfMeasure == existingRecord.uom } == null) {
                    // the parent record doesn't matter here as we are just deleting this record time so the record ID can be 0
                    personalRecordsRepository.deleteRecordTime(existingRecord.toRecordTime(0L)!!)
                }
            }
        }

        for (time in newRecordTimes) {
            personalRecordsRepository.updateRecordTime(time)
        }

        viewModelMutableEvent.value =
            EditPersonalRecordEvent.Success(stringProvider.getString(R.string.update_record_success))
    }
}
