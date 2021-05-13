package com.slapshotapps.swimyardagetracker.database

import androidx.room.*
import com.slapshotapps.swimyardagetracker.models.personalrecords.PersonalRecord
import com.slapshotapps.swimyardagetracker.models.personalrecords.PersonalRecordWithTimes
import com.slapshotapps.swimyardagetracker.models.personalrecords.RecordTime
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PersonalRecordsDAO {

    @Transaction
    @Query("SELECT * FROM `personal-records`")
    abstract fun getPersonalRecordsWithTimes(): Flow<List<PersonalRecordWithTimes>>

    @Transaction
    @Query("SELECT * FROM `personal-records`")
    abstract suspend fun getAllRecords(): List<PersonalRecordWithTimes>

    @Transaction
    @Query("SELECT * FROM `personal-records` WHERE id = :recordID")
    abstract fun getPersonalRecordWithTime(recordID: Long): Flow<PersonalRecordWithTimes>

    @Query("SELECT * FROM `personal-records` WHERE id = :recordID")
    abstract fun getPersonalRecord(recordID: Long): Flow<PersonalRecord>

    @Query("SELECT * FROM `record-time` WHERE id = :id")
    abstract fun getRecordTime(id: Long): RecordTime?

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
    abstract suspend fun deleteRecordTime(recordTime: RecordTime)

    suspend fun upsertRecordTimes(time: RecordTime) {
        if (getRecordTime(time.id) != null) {
            updateRecordTime(time)
        } else {
            insertRecordTime(time)
        }
    }

    suspend fun insertRecordWithTimes(record: PersonalRecord, times: List<RecordTime>) {
        val recordId = insertPersonalRecord(record)

        times.forEach { r -> r.recordID = recordId }

        insertRecordTimes(times)
    }
}
