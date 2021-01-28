package com.slapshotapps.swimyardagetracker.ui.records

import androidx.lifecycle.MutableLiveData
import com.slapshotapps.swimyardagetracker.models.personalrecords.PersonalRecord
import com.slapshotapps.swimyardagetracker.models.personalrecords.PersonalRecordWithTimes
import com.slapshotapps.swimyardagetracker.models.personalrecords.RecordTime
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutUoM
import com.slapshotapps.swimyardagetracker.repositories.WorkoutRepository
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class PersonalRecordsViewModel @Inject constructor(private val workoutRepository: WorkoutRepository){
    val allRecords : MutableLiveData<ArrayList<PersonalRecordItemViewModel>> by lazy {
        val testData = ArrayList<PersonalRecordItemViewModel>()

        val recordTimes = ArrayList<RecordTime>()
        recordTimes.add(RecordTime(1,
                1, Date(), WorkoutUoM.YARDS,
                0,0,22,44))

        testData.add(PersonalRecordItemViewModel(
                PersonalRecordWithTimes(
                        PersonalRecord( 1L, "Free", 50),
                        recordTimes
                ) ))

        testData.add(PersonalRecordItemViewModel(
                PersonalRecordWithTimes(
                        PersonalRecord( 1L, "Free", 100),
                        recordTimes
                ) ))

        testData.add(PersonalRecordItemViewModel(
                PersonalRecordWithTimes(
                        PersonalRecord( 1L, "Back", 50),
                        recordTimes
                ) ))


        MutableLiveData(testData)
    }
}


