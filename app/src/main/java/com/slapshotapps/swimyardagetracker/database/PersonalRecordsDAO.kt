package com.slapshotapps.swimyardagetracker.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.slapshotapps.swimyardagetracker.models.personalrecords.PersonalRecord
import com.slapshotapps.swimyardagetracker.models.personalrecords.PersonalRecordWithTimes
import com.slapshotapps.swimyardagetracker.models.personalrecords.RecordTime
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PersonalRecordsDAO {

    @Query("SELECT * FROM `personal-records`")
    abstract fun getPersonalRecordsWithTimes(): LiveData<List<PersonalRecordWithTimes>>

    @Query("SELECT * FROM `personal-records`")
    abstract suspend fun getAllRecords(): List<PersonalRecordWithTimes>

    @Query("SELECT * FROM `personal-records` WHERE id = :recordID")
    abstract fun getRecord(recordID: Long): Flow<PersonalRecordWithTimes>

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

    @Delete
    abstract suspend fun deleteRecordAndTimes(recordId: Long)

    @Delete
    abstract suspend fun deleteRecordTime(recordTime: RecordTime)

    suspend fun insertRecordWithTimes(record: PersonalRecord, times: List<RecordTime>) {
        val recordId = insertPersonalRecord(record)

        times.forEach { r -> r.recordID = recordId }

        insertRecordTimes(times)
    }
}
