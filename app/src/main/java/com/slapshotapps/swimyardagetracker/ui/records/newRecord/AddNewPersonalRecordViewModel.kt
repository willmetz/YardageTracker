package com.slapshotapps.swimyardagetracker.ui.records.newRecord

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.slapshotapps.swimyardagetracker.R
import com.slapshotapps.swimyardagetracker.models.personalrecords.PersonalRecord
import com.slapshotapps.swimyardagetracker.models.personalrecords.RecordTime
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutUoM
import com.slapshotapps.swimyardagetracker.repositories.PersonalRecordsRepository
import com.slapshotapps.swimyardagetracker.utils.StringProvider
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

sealed class AddNewPersonalRecordEvent {
    data class OnDateChanged(val formattedDate: String) : AddNewPersonalRecordEvent()
    data class OnNewRecordAddedSuccess(val msg: String) : AddNewPersonalRecordEvent()
    data class OnFailureToAddRecord(val msg: String) : AddNewPersonalRecordEvent()
    data class OnInvalidStroke(val msg: String) : AddNewPersonalRecordEvent()
    data class OnInvalidDistance(val msg: String) : AddNewPersonalRecordEvent()
    data class OnInvalidRecord(val msg: String) : AddNewPersonalRecordEvent()
    object OnReady : AddNewPersonalRecordEvent()
}

class AddNewPersonalRecordViewModel @Inject constructor(
    private val personalRecordsRepository: PersonalRecordsRepository,
    private val stringProvider: StringProvider
) {

    private val addPersonalRecordEvent: MutableLiveData<AddNewPersonalRecordEvent> =
        MutableLiveData(AddNewPersonalRecordEvent.OnReady)

    val viewModelEvent: LiveData<AddNewPersonalRecordEvent>
            get() = addPersonalRecordEvent

    private var unitOfMeasure: WorkoutUoM? = null
    private var recordDate: Date? = null

    private val dateFormatForRecord = SimpleDateFormat("MMMM d yyyy", Locale.US)

    suspend fun onAddNewRecord(stroke: String?, distance: Int?, time: TimeForPersonalRecord) {

        val uomForRecord = unitOfMeasure
        val dateOfRecord = recordDate

        when {
            distance == null || distance <= 0 ->
                addPersonalRecordEvent.value =
                    AddNewPersonalRecordEvent.OnInvalidDistance(stringProvider.getString(R.string.distance_required))
            stroke == null || stroke.isBlank() ->
                addPersonalRecordEvent.value =
                    AddNewPersonalRecordEvent.OnInvalidStroke(stringProvider.getString(R.string.stroke_required))
            time.minutes <= 0 && time.seconds <= 0 && time.milliseconds <= 0 ->
                addPersonalRecordEvent.value =
                    AddNewPersonalRecordEvent.OnInvalidRecord(stringProvider.getString(R.string.time_required))
            uomForRecord == null ->
                addPersonalRecordEvent.value =
                    AddNewPersonalRecordEvent.OnInvalidRecord(stringProvider.getString(R.string.unit_of_measure_required))
            dateOfRecord == null ->
                addPersonalRecordEvent.value =
                    AddNewPersonalRecordEvent.OnInvalidRecord(stringProvider.getString(R.string.date_required))
            else -> {
                // check to see if the event is in the DB already
                val existingRecords = personalRecordsRepository.getAllRecords()

                val matchingRecord = existingRecords.firstOrNull {
                    it.record.distance == distance && it.record.stroke == stroke
                }

                if (matchingRecord != null) {
                    addPersonalRecordEvent.value = AddNewPersonalRecordEvent
                            .OnInvalidRecord(stringProvider.getString(R.string.record_already_exists_error))
                    return
                }

                val newRecord = PersonalRecord(stroke = stroke, distance = distance)
                val newRecordTime = RecordTime(recordID = 0, date = dateOfRecord,
                        hours = 0, minutes = time.minutes, seconds = time.seconds,
                        milliseconds = time.milliseconds, unitOfMeasure = uomForRecord)

                personalRecordsRepository.addNewRecordWithTime(newRecord, newRecordTime)

                addPersonalRecordEvent.value = AddNewPersonalRecordEvent
                        .OnNewRecordAddedSuccess(stringProvider.getString(R.string.record_added_success, distance, stroke))
            }
        }
    }

    fun onDateChanged(newRecordDate: Date) {
        addPersonalRecordEvent.value = AddNewPersonalRecordEvent.OnDateChanged(dateFormatForRecord.format(newRecordDate))
        recordDate = newRecordDate
    }

    fun updateUnitOfMeasure(newUnitOfMeasure: String) {
        unitOfMeasure = WorkoutUoM.fromString(newUnitOfMeasure)
    }
}
