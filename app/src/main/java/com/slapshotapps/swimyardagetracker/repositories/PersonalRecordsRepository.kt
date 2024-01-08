package com.slapshotapps.swimyardagetracker.repositories

import com.slapshotapps.swimyardagetracker.database.WorkoutDatabase
import com.slapshotapps.swimyardagetracker.models.personalrecords.*
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class PersonalRecordsRepository @Inject constructor(private val workoutDatabase: WorkoutDatabase) {

    suspend fun addNewRecordWithTime(record: PersonalRecord, time: RecordTime) {
        addNewRecordWithTimes(record, listOf(time))
    }

    suspend fun addNewRecordWithTimes(record: PersonalRecord, times: List<RecordTime>) {
        workoutDatabase.personalRecordDao().insertRecordWithTimes(record, times)
    }

    suspend fun deleteRecord(record: PersonalRecord) {
        workoutDatabase.personalRecordDao().deleteRecordAndTimes(record)
    }

    suspend fun deleteRecord(recordId: Long) {
        val recordToDelete = workoutDatabase.personalRecordDao().getPersonalRecord(recordId).firstOrNull()
        if (recordToDelete != null) {
            workoutDatabase.personalRecordDao().deleteRecordAndTimes(recordToDelete)
        }
    }

    suspend fun deleteRecordTime(time: RecordTime) {
        workoutDatabase.personalRecordDao().deleteRecordTime(time)
    }

    suspend fun updateRecordTime(time: RecordTime) {
        workoutDatabase.personalRecordDao().upsertRecordTimes(time)
    }

    suspend fun addTimeForRecord(record: PersonalRecord, time: RecordTime) {
        time.recordID = record.id
        workoutDatabase.personalRecordDao().insertRecordTime(time)
    }

    suspend fun updateRecord(record: PersonalRecord) {
        workoutDatabase.personalRecordDao().updatePersonalRecord(record)
    }

    fun getAllRecordsWithTimes(): Flow<List<PersonalRecordWithTimes>> {
        return workoutDatabase.personalRecordDao().getPersonalRecordsWithTimes()
    }

    suspend fun getAllRecords(): List<PersonalRecordWithTimes> {
        return workoutDatabase.personalRecordDao().getAllRecords()
    }

    fun getRecord(recordID: Long): Flow<YardageTrackerPersonalRecord> {
        val dbRecord = workoutDatabase.personalRecordDao().getPersonalRecordWithTime(recordID)

        return dbRecord.map {
            YardageTrackerPersonalRecord.fromEntities(it.record, it.times)
        }
    }
}
