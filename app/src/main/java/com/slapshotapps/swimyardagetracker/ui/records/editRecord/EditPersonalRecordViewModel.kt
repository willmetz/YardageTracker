package com.slapshotapps.swimyardagetracker.ui.records.editRecord

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.slapshotapps.swimyardagetracker.models.personalrecords.YardageTrackerPersonalRecord
import com.slapshotapps.swimyardagetracker.models.personalrecords.YardageTrackerRecordTime
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutUoM
import com.slapshotapps.swimyardagetracker.repositories.PersonalRecordsRepository
import com.slapshotapps.swimyardagetracker.utils.StringProvider
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

sealed class EditPersonalRecordEvent {
    data class InitDisplay(val displayData: EditableRecordModel) : EditPersonalRecordEvent()
    data class DeleteRecordError(val msg: String) : EditPersonalRecordEvent()
    data class Success(val msg: String) : EditPersonalRecordEvent()
    object Loading : EditPersonalRecordEvent()
}

class EditPersonalRecordViewModel @Inject constructor(
    private val personalRecordsRepository: PersonalRecordsRepository,
    private val stringProvider: StringProvider
) {

    private val dateFormatForRecord = SimpleDateFormat("MMMM d yyyy", Locale.US)

    private var personalRecord: YardageTrackerPersonalRecord ? = null
    private val viewModelMutableEvent: MutableLiveData<EditPersonalRecordEvent> = MutableLiveData(EditPersonalRecordEvent.Loading)

    val viewModelEvent: LiveData<EditPersonalRecordEvent>
        get() = viewModelMutableEvent

    fun init(record: YardageTrackerPersonalRecord) {
        personalRecord = record
        createViewDataForRecord()
    }

    private fun createViewDataForRecord() {
        val record = personalRecord ?: return

        val eventName = "${record.distance} ${record.stroke}"

        val yardageRecord = record.recordTimes.firstOrNull { record -> record.uom == WorkoutUoM.YARDS }
        val metricRecord = record.recordTimes.firstOrNull { record -> record.uom == WorkoutUoM.METERS }

        val displayData = EditableRecordModel(eventName, buildTimeRecordModel(yardageRecord, WorkoutUoM.YARDS),
            buildTimeRecordModel(metricRecord, WorkoutUoM.METERS))

        viewModelMutableEvent.value = EditPersonalRecordEvent.InitDisplay(displayData)
    }

    private fun buildTimeRecordModel(time: YardageTrackerRecordTime?, uom: WorkoutUoM): EditableTimeRecordModel {
        if (time == null) {
            return EditableTimeRecordModel(false, "", "", "", Date(), "")
        }
        val minutes = String.format("%d", time.minutes)
        val seconds = String.format("%02d", time.seconds)
        val milliseconds = String.format("%02d", time.milliseconds)

        val formattedDate = dateFormatForRecord.format(time.date)

        return EditableTimeRecordModel(true, minutes, seconds, milliseconds, time.date, formattedDate)
    }
}
