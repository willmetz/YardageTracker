package com.slapshotapps.swimyardagetracker.database

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.slapshotapps.swimyardagetracker.database.extensions.blockingObserve
import com.slapshotapps.swimyardagetracker.models.personalrecords.PersonalRecord
import com.slapshotapps.swimyardagetracker.models.personalrecords.RecordTime
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutUoM
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import rules.InstrumentedImmediateSchedulersRule
import java.util.*
import kotlin.collections.ArrayList


@RunWith(AndroidJUnit4::class)
class PersonalRecordDatabaseTest {

    @get:Rule
    val rule = InstrumentedImmediateSchedulersRule()

    lateinit var dao: PersonalRecordsDAO
    lateinit var database: WorkoutDatabase

    @Before
    fun setUp() {

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, WorkoutDatabase::class.java).build()
        dao = database.personalRecordDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun test_insertRecordsWithTimesAndRetrieveThem() = runBlocking{
        val record = PersonalRecord(stroke = "Free", distance = 100)
        val times = ArrayList<RecordTime>();
        times.add(RecordTime(
                recordID = 0, date = Date(),
                unitOfMeasure = WorkoutUoM.YARDS,
                hours = 0, minutes = 0, seconds = 55, milliseconds = 23))

        times.add(RecordTime(
                recordID = 0, date = Date(),
                unitOfMeasure = WorkoutUoM.METERS,
                hours = 0, minutes = 0, seconds = 59, milliseconds = 45))

        dao.insertRecordWithTimes(record, times)

        val result = dao.getPersonalRecordsWithTimes().blockingObserve()

        assertEquals(record.distance,result?.get(0)?.record?.distance)
        assertEquals(record.stroke,result?.get(0)?.record?.stroke)

        assertEquals(times.get(0).seconds, result?.get(0)?.times?.get(0)?.seconds)
        assertEquals(times.get(0).milliseconds, result?.get(0)?.times?.get(0)?.milliseconds)

        assertEquals(times.get(1).seconds, result?.get(1)?.times?.get(1)?.seconds)
        assertEquals(times.get(1).milliseconds, result?.get(1)?.times?.get(1)?.milliseconds)
    }
}