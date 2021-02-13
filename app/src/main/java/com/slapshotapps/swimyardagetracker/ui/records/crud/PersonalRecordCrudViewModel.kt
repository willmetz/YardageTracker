package com.slapshotapps.swimyardagetracker.ui.records.crud

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.slapshotapps.swimyardagetracker.models.personalrecords.PersonalRecord
import com.slapshotapps.swimyardagetracker.models.personalrecords.RecordTime
import com.slapshotapps.swimyardagetracker.repositories.PersonalRecordsRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.*

sealed class PersonalRecordCrudEvent {
    data class OnDateChanged(val formattedDate: String) : PersonalRecordCrudEvent()
    data class OnNewRecordAddedSuccess(val msg: String) : PersonalRecordCrudEvent()
    data class OnFailureToAddRecord(val msg: String) : PersonalRecordCrudEvent()
    data class OnInvalidStroke(val msg: String) : PersonalRecordCrudEvent()
    data class OnInvalidDistance(val msg: String) : PersonalRecordCrudEvent()
    data class OnInvalidUnitOfMeasure(val msg: String) : PersonalRecordCrudEvent()
    data class OnInvalidTime(val msg: String) : PersonalRecordCrudEvent()
    data class OnRecordAlreadyExists(val msg: String) : PersonalRecordCrudEvent()
    object OnLoading : PersonalRecordCrudEvent()
}

class PersonalRecordCrudViewModel @Inject constructor(private val personalRecordsRepository: PersonalRecordsRepository) {

    private val personalRecordCrudEvent: MutableLiveData<PersonalRecordCrudEvent> = MutableLiveData(PersonalRecordCrudEvent.OnLoading)

    val viewModelEvent: LiveData<PersonalRecordCrudEvent>
            get() = personalRecordCrudEvent

    private val dateFormatForRecord = SimpleDateFormat("MMMM d yyyy", Locale.US)

    suspend fun onAddNewRecord(record: RawRecord) {

        when {
            record.stroke == null || record.stroke.isBlank() ->
                personalRecordCrudEvent.value = PersonalRecordCrudEvent.OnInvalidStroke("Stroke is required.")
            record.distance == null || record.distance <= 0 ->
                personalRecordCrudEvent.value = PersonalRecordCrudEvent.OnInvalidDistance("Distance is required.")
            record.unitOfMeasure == null ->
                personalRecordCrudEvent.value = PersonalRecordCrudEvent.OnInvalidUnitOfMeasure("Select unit of measure.")
            record.time.minutes == 0 && record.time.seconds == 0 && record.time.milliseconds == 0 ->
                personalRecordCrudEvent.value = PersonalRecordCrudEvent.OnInvalidTime("Please enter a time for the record.")
            else -> {
                // check to see if the event is in the DB already
                val existingRecords = personalRecordsRepository.getAllRecords()

                val matchingRecord = existingRecords.firstOrNull {
                    it.record.distance == record.distance && it.record.stroke == record.stroke
                }

                if (matchingRecord != null) {
                    personalRecordCrudEvent.value = PersonalRecordCrudEvent
                            .OnRecordAlreadyExists("This event already has a record, please edit that record.")
                    return
                }

                val newRecord = PersonalRecord(stroke = record.stroke, distance = record.distance)
                val newRecordTime = RecordTime(recordID = 0, date = record.dateOfRecord,
                        hours = 0, minutes = record.time.minutes, seconds = record.time.seconds,
                        milliseconds = record.time.milliseconds, unitOfMeasure = record.unitOfMeasure)

                personalRecordsRepository.addNewRecordWithTime(newRecord, newRecordTime)

                personalRecordCrudEvent.value = PersonalRecordCrudEvent
                        .OnNewRecordAddedSuccess("Added record for ${record.distance} ${record.stroke}!!")
            }
        }
    }

    fun onDateChanged(newRecordDate: Date) {
        personalRecordCrudEvent.value = PersonalRecordCrudEvent.OnDateChanged(dateFormatForRecord.format(newRecordDate))
    }
}
