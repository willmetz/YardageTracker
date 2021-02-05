package com.slapshotapps.swimyardagetracker.ui.records.crud

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.slapshotapps.swimyardagetracker.models.personalrecords.PersonalRecord
import com.slapshotapps.swimyardagetracker.models.personalrecords.RecordTime
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutUoM
import com.slapshotapps.swimyardagetracker.repositories.PersonalRecordsRepository
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject
import kotlin.math.min
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

data class RawRecord @ExperimentalTime constructor(val stroke: String?,
                                                   val distance: Int?,
                                                   val dateOfRecord: Date?,
                                                   val unitOfMeasure: WorkoutUoM?,
                                                   val recordTime: Duration?)

enum class RecordCrudResult{
    success,
    eventAlreadyExists,
    invalidEvent,
    invalidDistance,
    invalidDate,
    invalidUnitOfMeasure,
    invalidTime,
    recordAdded
}

class PersonalRecordCrudViewModel @Inject constructor(private val personalRecordsRepository: PersonalRecordsRepository){


    val viewModelState : MutableLiveData<RecordCrudResult> = MutableLiveData(RecordCrudResult.success)

    //probably won't need this
    protected val scope = CoroutineScope(
            Job() + Dispatchers.Main
    )

    //probably won't need this
    fun onCleared() {
        scope.cancel("exiting view model")
    }


    @ExperimentalTime
    suspend fun onAddNewRecord(record: RawRecord){
        if(record.stroke == null || record.stroke.isBlank()){
            viewModelState.value = RecordCrudResult.invalidEvent
            return
        }

        if(record.distance == null || record.distance <= 0){
            viewModelState.value = RecordCrudResult.invalidDistance
            return
        }

        if(record.dateOfRecord == null){
            viewModelState.value = RecordCrudResult.invalidDate
            return
        }

        if(record.unitOfMeasure == null){
            viewModelState.value = RecordCrudResult.invalidUnitOfMeasure
            return
        }

        if(record.recordTime == null){
            viewModelState.value = RecordCrudResult.invalidTime
            return
        }

        //check to see if the event is in the DB already
        val existingRecords = personalRecordsRepository.getAllRecords()

        val matchingRecord = existingRecords.firstOrNull{
            it.record.distance == record.distance && it.record.stroke == record.stroke
        }

        if(matchingRecord != null) {
            viewModelState.value = RecordCrudResult.eventAlreadyExists
            return
        }

        val newRecord = PersonalRecord(stroke = record.stroke, distance = record.distance)
        var hours = 0
        var minutes = 0
        var seconds = 0
        var milliseconds = 0
        record.recordTime.toComponents { h, m, s, ns ->
            hours = h
            minutes = m
            seconds = s
            milliseconds = ns/1000
        }
        val newRecordTime = RecordTime(recordID = 0, date = record.dateOfRecord,
                hours = hours, minutes = minutes, seconds = seconds, milliseconds = milliseconds, unitOfMeasure = record.unitOfMeasure)

        personalRecordsRepository.addNewRecordWithTime(newRecord, newRecordTime)

        RecordCrudResult.recordAdded
    }
}