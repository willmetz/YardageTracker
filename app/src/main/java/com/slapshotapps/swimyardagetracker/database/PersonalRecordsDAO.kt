package com.slapshotapps.swimyardagetracker.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.slapshotapps.swimyardagetracker.models.personalrecords.PersonalRecord
import com.slapshotapps.swimyardagetracker.models.personalrecords.PersonalRecordWithTimes
import com.slapshotapps.swimyardagetracker.models.personalrecords.RecordTime
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutSet


@Dao
interface PersonalRecordsDAO {

    @Transaction
    @Query("SELECT * FROM `personal-records`")
    fun getPersonalRecordsWithTimes(): LiveData<List<PersonalRecordWithTimes>>

    @Insert
    fun AddPersonalRecordWithTime(record: PersonalRecord, times: List<RecordTime>)

    @Update
    fun UpdatePersonalRecord(record: PersonalRecord)

    @Update
    fun UpdateRecordTime(time: RecordTime)


}