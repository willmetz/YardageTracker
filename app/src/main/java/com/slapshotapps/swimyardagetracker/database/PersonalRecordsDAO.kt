package com.slapshotapps.swimyardagetracker.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.slapshotapps.swimyardagetracker.models.personalrecords.PersonalRecord
import com.slapshotapps.swimyardagetracker.models.personalrecords.PersonalRecordWithTimes
import com.slapshotapps.swimyardagetracker.models.personalrecords.RecordTime



@Dao
abstract class PersonalRecordsDAO {

    @Query("SELECT * FROM `personal-records`")
    abstract fun getPersonalRecordsWithTimes(): LiveData<List<PersonalRecordWithTimes>>

    @Insert
    suspend abstract fun insertPersonalRecord(record: PersonalRecord): Long

    @Insert
    suspend abstract fun insertRecordTime(time: RecordTime)

    @Insert
    suspend abstract fun insertRecordTimes(time: List<RecordTime>)

    @Update
    suspend abstract fun updatePersonalRecord(record: PersonalRecord)

    @Update
    suspend abstract fun updateRecordTime(time: RecordTime)

    @Delete
    abstract fun deleteRecordAndTimes(record: PersonalRecord)

    suspend fun insertRecordWithTimes(record: PersonalRecord, times: List<RecordTime>)
    {
        val recordId = insertPersonalRecord(record)

        times.forEach{r -> r.recordID = recordId}

        insertRecordTimes(times)
    }


}