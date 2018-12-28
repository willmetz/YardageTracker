package com.slapshotapps.swimyardagetracker.database

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.slapshotapps.swimyardagetracker.models.workout.Workout
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutUoM
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import rules.InstrumentedImmediateSchedulersRule
import java.util.*

@RunWith(AndroidJUnit4::class)
class WorkoutDatabaseTest {

    @get:Rule
    val rule = InstrumentedImmediateSchedulersRule()

    lateinit var dao: WorkoutDAO
    lateinit var database: WorkoutDatabase

    @Before
    fun setUp() {

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, WorkoutDatabase::class.java).build()
        dao = database.workoutDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun test_getLatestWorkout() {

        //given
        val oldDate = Calendar.getInstance()
        oldDate.set(2015, Calendar.JANUARY, 1)

        val newDate = Calendar.getInstance()
        newDate.set(2015, Calendar.FEBRUARY, 1)

        val oldWorkout = Workout(WorkoutUoM.YARDS, oldDate.time, oldDate.time)
        val newWorkout = Workout(WorkoutUoM.METERS, newDate.time, newDate.time)

        dao.insert(newWorkout).test()
        val expectedWorkout = dao.insert(oldWorkout).test()
        val expectedID = expectedWorkout.values()[0]

        //when
        val latestWorkout = dao.getLatestWorkout().test()


        //then
        latestWorkout.assertComplete()
        latestWorkout.assertValueCount(1)
        val retrievedWorkouts = latestWorkout.values()

        assert(retrievedWorkouts.count() == 1)
        assert(retrievedWorkouts[0].uoM == WorkoutUoM.METERS)
        assert(retrievedWorkouts[0].id == expectedID)
    }
}