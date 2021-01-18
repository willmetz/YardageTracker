package com.slapshotapps.swimyardagetracker.ui.records

import androidx.lifecycle.MutableLiveData
import com.slapshotapps.swimyardagetracker.models.personalrecords.PersonalRecord
import com.slapshotapps.swimyardagetracker.models.personalrecords.RecordTime
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutUoM
import com.slapshotapps.swimyardagetracker.repositories.WorkoutRepository
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class PersonalRecordsViewModel @Inject constructor(private val workoutRepository: WorkoutRepository){
    val allRecords : MutableLiveData<ArrayList<PersonalRecordItemViewModel>> by lazy {
        val testData = ArrayList<PersonalRecordItemViewModel>()

        testData.add(PersonalRecordItemViewModel( PersonalRecord( "Free",
                100, arrayListOf( RecordTime(Date(), WorkoutUoM.YARDS,
                0,0,55,75 ))) ))

        testData.add(PersonalRecordItemViewModel( PersonalRecord( "Fly",
                100, arrayListOf( RecordTime(Date(), WorkoutUoM.YARDS,
                0,1,9,0 ))) ))

        testData.add(PersonalRecordItemViewModel( PersonalRecord( "Free",
                200, arrayListOf(
                RecordTime(Date(), WorkoutUoM.YARDS,
                0,1,55,75 ),
                RecordTime(Date(), WorkoutUoM.METERS,
                        0,2,10,0 ))) ))

        MutableLiveData(testData)
    }
}


