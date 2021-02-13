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

    @Query("SELECT * FROM `personal-records`")
    abstract suspend fun getAllRecords(): List<PersonalRecordWithTimes>

    @Insert
    abstract suspend fun insertPersonalRecord(record: PersonalRecord): Long

    @Insert
    abstract suspend fun insertRecordTime(time: RecordTime)

    @Insert
    abstract suspend fun insertRecordTimes(time: List<RecordTime>)

    @Update
    abstract suspend fun updatePersonalRecord(record: PersonalRecord)

    @Update
    abstract suspend fun updateRecordTime(time: RecordTime)

    @Delete
    abstract suspend fun deleteRecordAndTimes(record: PersonalRecord)

    suspend fun insertRecordWithTimes(record: PersonalRecord, times: List<RecordTime>) {
        val recordId = insertPersonalRecord(record)

        times.forEach { r -> r.recordID = recordId }

        insertRecordTimes(times)
    }
}
