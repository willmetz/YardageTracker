package com.slapshotapps.swimyardagetracker.repositories

import androidx.lifecycle.LiveData
import com.slapshotapps.swimyardagetracker.database.WorkoutDatabase
import com.slapshotapps.swimyardagetracker.models.personalrecords.*
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
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
        workoutDatabase.personalRecordDao().deleteRecordAndTimes(recordId)
    }

    suspend fun deleteRecordTime(time: RecordTime) {
        workoutDatabase.personalRecordDao().deleteRecordTime(time)
    }

    suspend fun updateRecordTime(time: RecordTime) {
        workoutDatabase.personalRecordDao().updateRecordTime(time)
    }

    suspend fun addTimeForRecord(record: PersonalRecord, time: RecordTime) {
        time.recordID = record.id
        workoutDatabase.personalRecordDao().insertRecordTime(time)
    }

    suspend fun updateRecord(record: PersonalRecord) {
        workoutDatabase.personalRecordDao().updatePersonalRecord(record)
    }

    fun getAllRecordsWithTimes(): LiveData<List<PersonalRecordWithTimes>> {
        return workoutDatabase.personalRecordDao().getPersonalRecordsWithTimes()
    }

    suspend fun getAllRecords(): List<PersonalRecordWithTimes> {
        return workoutDatabase.personalRecordDao().getAllRecords()
    }

    fun getRecord(recordID: Long): Flow<YardageTrackerPersonalRecord> {
        val dbRecord = workoutDatabase.personalRecordDao().getRecord(recordID)

        return dbRecord.map {
            YardageTrackerPersonalRecord.fromEntities(it.record, it.times)
        }
    }
}
