package com.slapshotapps.swimyardagetracker.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.slapshotapps.swimyardagetracker.database.extensions.blockingObserve
import com.slapshotapps.swimyardagetracker.models.personalrecords.PersonalRecord
import com.slapshotapps.swimyardagetracker.models.personalrecords.PersonalRecordWithTimes
import com.slapshotapps.swimyardagetracker.models.personalrecords.RecordTime
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutUoM
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList


@RunWith(AndroidJUnit4::class)
class PersonalRecordDatabaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    lateinit var dao: PersonalRecordsDAO
    lateinit var database: WorkoutDatabase

    @Before
    fun setUp() {

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, WorkoutDatabase::class.java)
                .setTransactionExecutor(Executors.newSingleThreadExecutor())
                .build()
        dao = database.personalRecordDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun test_insertRecordsWithTimesAndRetrieveThem() = runBlocking{
        val record = generateRecordsWithRandomTimes("Free", 100);

        dao.insertRecordWithTimes(record.record, record.times)

        val result = dao.getPersonalRecordsWithTimes().blockingObserve()

        assertEquals(record.record.distance,result?.get(0)?.record?.distance)
        assertEquals(record.record.stroke,result?.get(0)?.record?.stroke)

        assertEquals(record.times.get(0).seconds, result?.get(0)?.times?.get(0)?.seconds)
        assertEquals(record.times.get(0).milliseconds, result?.get(0)?.times?.get(0)?.milliseconds)

        assertEquals(record.times.get(1).seconds, result?.get(0)?.times?.get(1)?.seconds)
        assertEquals(record.times.get(1).milliseconds, result?.get(0)?.times?.get(1)?.milliseconds)
    }

    @Test
    fun test_deleteRecordsAlsoDeletesTimes() = runBlocking{
        val record = generateRecordsWithRandomTimes("Free", 100);
        val record2 = generateRecordsWithRandomTimes("Fly", 50);

        dao.insertRecordWithTimes(record.record, record.times)
        dao.insertRecordWithTimes(record2.record, record2.times)

        val result = dao.getPersonalRecordsWithTimes().blockingObserve()

        val recordToDelete = result?.first { r -> r.record.stroke == record.record.stroke }

        dao.deleteRecordAndTimes(recordToDelete!!.record)

        //verify there is only record2 left, with the times
        val remainingRecord = dao.getPersonalRecordsWithTimes().blockingObserve()

        assertEquals(record2.record.distance,remainingRecord?.get(0)?.record?.distance)
        assertEquals(record2.record.stroke,remainingRecord?.get(0)?.record?.stroke)

        assertEquals(record2.times.get(0).seconds, remainingRecord?.get(0)?.times?.get(0)?.seconds)
        assertEquals(record2.times.get(0).milliseconds, remainingRecord?.get(0)?.times?.get(0)?.milliseconds)

        assertEquals(record2.times.get(1).seconds, remainingRecord?.get(0)?.times?.get(1)?.seconds)
        assertEquals(record2.times.get(1).milliseconds, remainingRecord?.get(0)?.times?.get(1)?.milliseconds)

    }

    @Test
    fun test_updateRecord() = runBlocking {
        val originalRecord = generateRecordsWithRandomTimes("Free", 100);

        dao.insertRecordWithTimes(originalRecord.record, originalRecord.times)

        val recordFromDB = dao.getPersonalRecordsWithTimes().blockingObserve()

        val newRecord = PersonalRecord(recordFromDB!!.get(0).record.id, "Fly", recordFromDB.get(0).record.distance)

        dao.updatePersonalRecord(newRecord)

        val updatedRecordFromDb = dao.getPersonalRecordsWithTimes().blockingObserve()

        val updatedRecord = updatedRecordFromDb!!.get(0)
        assertEquals(originalRecord.record.distance, updatedRecord.record.distance)
        assertEquals(newRecord.stroke, updatedRecord.record.stroke)

        assertEquals(originalRecord.times[0].unitOfMeasure, updatedRecord.times[0].unitOfMeasure)
        assertEquals(originalRecord.times[0].seconds, updatedRecord.times[0].seconds)

        assertEquals(originalRecord.times[1].unitOfMeasure, updatedRecord.times[1].unitOfMeasure)
        assertEquals(originalRecord.times[1].seconds, updatedRecord.times[1].seconds)
    }

    @Test
    fun test_addTimesToRecord() = runBlocking {
        val originalRecord = generateRecordsWithRandomTimes("Free", 100);

        dao.insertRecordWithTimes(originalRecord.record, originalRecord.times)

        val recordFromDB = dao.getPersonalRecordsWithTimes().blockingObserve()

        val addedTime = RecordTime(recordID = recordFromDB!![0].record.id, unitOfMeasure = WorkoutUoM.YARDS, date = Date(),
            hours = 0, minutes = 2, seconds = 32, milliseconds = 0)

        dao.insertRecordTime(addedTime)

        val recordsWithAddedTime = dao.getPersonalRecordsWithTimes().blockingObserve()

        val retrievedTimeRecords = recordsWithAddedTime!![0].times
        assertEquals(3, retrievedTimeRecords.count())
        assertTrue(retrievedTimeRecords.find { r -> r.unitOfMeasure == WorkoutUoM.YARDS && r.seconds == addedTime.seconds } != null)
        assertTrue(retrievedTimeRecords.find { r -> r.unitOfMeasure == WorkoutUoM.YARDS && r.seconds == addedTime.seconds } != null)
        assertTrue(retrievedTimeRecords.find { r -> r.unitOfMeasure == originalRecord.times[0].unitOfMeasure
                && r.seconds == originalRecord.times[0].seconds } != null)

        assertTrue(retrievedTimeRecords.find { r -> r.unitOfMeasure == originalRecord.times[1].unitOfMeasure
                && r.seconds == originalRecord.times[1].seconds } != null)
    }

    private fun generateRecordsWithRandomTimes(stroke: String, distance: Int) : PersonalRecordWithTimes {

        val record = PersonalRecord(stroke = stroke, distance = distance)
        val times = ArrayList<RecordTime>();
        times.add(RecordTime(
                recordID = 0, date = Date(),
                unitOfMeasure = WorkoutUoM.YARDS,
                hours = 0, minutes = 0, seconds = Math.random().toInt() % 60, milliseconds = Math.random().toInt() % 100))

        times.add(RecordTime(
                recordID = 0, date = Date(),
                unitOfMeasure = WorkoutUoM.METERS,
                hours = 0, minutes = 0, seconds = Math.random().toInt() % 60, milliseconds = Math.random().toInt() % 100))

        return PersonalRecordWithTimes(record, times)
    }
}