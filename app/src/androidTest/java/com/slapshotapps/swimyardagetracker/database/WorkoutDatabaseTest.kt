package com.slapshotapps.swimyardagetracker.database

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.slapshotapps.swimyardagetracker.models.workout.Workout
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutSet
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutUoM
import java.util.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import rules.InstrumentedImmediateSchedulersRule

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

        // given
        val oldDate = Calendar.getInstance()
        oldDate.set(2015, Calendar.JANUARY, 1)

        val newDate = Calendar.getInstance()
        newDate.set(2015, Calendar.FEBRUARY, 1)

        val oldWorkout = Workout(WorkoutUoM.YARDS, oldDate.time, oldDate.time)
        val newWorkout = Workout(WorkoutUoM.METERS, newDate.time, newDate.time)

        val expectedWorkout = dao.insert(newWorkout).test()
        val expectedID = expectedWorkout.values()[0]
        dao.insert(oldWorkout).test()

        // when
        val latestWorkout = dao.getLatestWorkout().test()

        // then
        latestWorkout.assertComplete()
        latestWorkout.assertValueCount(1)
        val retrievedWorkouts = latestWorkout.values()

        assertEquals(1, retrievedWorkouts.count())
        assertEquals(newWorkout.uoM, retrievedWorkouts[0].uoM)
        assertEquals(expectedID, retrievedWorkouts[0].id)
    }

    @Test
    fun test_noWorkoutFound() {
        // when
        val latestWorkout = dao.getLatestWorkout().test()

        // then
        latestWorkout.assertComplete()
        latestWorkout.assertValueCount(0)
    }

    @Test
    fun test_getSetsForWorkout() {
        // given
        val workout1 = Workout(WorkoutUoM.METERS, Date(), Date())
        val workout2 = Workout(WorkoutUoM.YARDS, Date(), Date())

        val workoutOneId = dao.insert(workout1).test().values()[0]
        val workoutTwoId = dao.insert(workout2).test().values()[0]

        val set1 = WorkoutSet(0L, workoutOneId, 1, 100, "Free", Date(), Date())
        val set2 = WorkoutSet(0L, workoutTwoId, 2, 200, "Free", Date(), Date())
        val set3 = WorkoutSet(0L, workoutTwoId, 1, 300, "Free", Date(), Date())

        val sets = ArrayList<WorkoutSet>()

        sets.add(set1)
        sets.add(set2)
        sets.add(set3)

        dao.insert(sets).test()

        // when
        val setsForWorkout = dao.getSetsForWorkout(workoutTwoId).test()

        // then
        setsForWorkout.assertComplete()
        val workoutSetsFound = setsForWorkout.values()[0]

        assertEquals(2, workoutSetsFound.count())
        assertEquals(set2.distance, workoutSetsFound[0].distance)
        assertEquals(set3.distance, workoutSetsFound[1].distance)
    }

    @Test
    fun test_getSetsForWorkoutIsEmptyIfNoneMatch() {
        // given
        val workout1 = Workout(WorkoutUoM.METERS, Date(), Date())
        val workout2 = Workout(WorkoutUoM.YARDS, Date(), Date())

        val workoutOneId = dao.insert(workout1).test().values()[0]
        val workoutTwoId = dao.insert(workout2).test().values()[0]

        val set1 = WorkoutSet(0L, workoutOneId, 1, 100, "Free", Date(), Date())
        val set2 = WorkoutSet(0L, workoutOneId, 2, 200, "Free", Date(), Date())
        val set3 = WorkoutSet(0L, workoutTwoId, 1, 100, "Free", Date(), Date())

        val sets = ArrayList<WorkoutSet>()

        sets.add(set1)
        sets.add(set2)
        sets.add(set3)

        dao.insert(sets).test()

        // when
        val setsForWorkout = dao.getSetsForWorkout(5L).test()

        // then
        setsForWorkout.assertComplete()
        val workoutSetsFound = setsForWorkout.values()[0]

        assertEquals(0, workoutSetsFound.count())
    }

    @Test
    fun test_getWorkoutsSinceDate() {
        // given
        val calendarTestSinceDate = Calendar.getInstance()
        calendarTestSinceDate.set(2016, Calendar.JUNE, 8, 0, 0, 0)

        val workoutOneDate = calendarTestSinceDate.clone() as Calendar
        workoutOneDate.add(Calendar.YEAR, 1)

        val workoutTwoDate = calendarTestSinceDate.clone() as Calendar
        workoutTwoDate.add(Calendar.DAY_OF_MONTH, 1)

        val workoutThreeDate = calendarTestSinceDate.clone() as Calendar
        workoutThreeDate.add(Calendar.DAY_OF_MONTH, -1)

        val workout1 = Workout(WorkoutUoM.METERS, workoutOneDate.time, Date())
        val workout2 = Workout(WorkoutUoM.YARDS, workoutTwoDate.time, Date())
        val workout3 = Workout(WorkoutUoM.YARDS, workoutThreeDate.time, Date())

        dao.insert(workout1).test()
        dao.insert(workout2).test()
        dao.insert(workout3).test()

        // when
        val workoutsSinceDate = dao.getWorkoutCountFromDate(calendarTestSinceDate.time.time).test()

        // then
        workoutsSinceDate.assertComplete()
        val workoutCountSinceDate = workoutsSinceDate.values()[0]
        assertEquals(2, workoutCountSinceDate)
    }

    @Test
    fun test_getWorkoutsSinceDateNoneFound() {
        // given
        val calendarTestSinceDate = Calendar.getInstance()
        calendarTestSinceDate.set(2016, Calendar.JUNE, 8, 0, 0, 0)

        val workoutOneDate = calendarTestSinceDate.clone() as Calendar
        workoutOneDate.add(Calendar.YEAR, 1)

        val workoutTwoDate = calendarTestSinceDate.clone() as Calendar
        workoutTwoDate.add(Calendar.DAY_OF_MONTH, 1)

        val workoutThreeDate = calendarTestSinceDate.clone() as Calendar
        workoutThreeDate.add(Calendar.DAY_OF_MONTH, -1)

        calendarTestSinceDate.add(Calendar.YEAR, 2)

        val workout1 = Workout(WorkoutUoM.METERS, workoutOneDate.time, Date())
        val workout2 = Workout(WorkoutUoM.YARDS, workoutTwoDate.time, Date())
        val workout3 = Workout(WorkoutUoM.YARDS, workoutThreeDate.time, Date())

        dao.insert(workout1).test()
        dao.insert(workout2).test()
        dao.insert(workout3).test()

        // when
        val workoutsSinceDate = dao.getWorkoutCountFromDate(calendarTestSinceDate.time.time).test()

        // then
        workoutsSinceDate.assertComplete()
        val workoutCountSinceDate = workoutsSinceDate.values()[0]
        assertEquals(0, workoutCountSinceDate)
    }

    @Test
    fun test_getWorkoutSeteSinceDateWithUoM() {
        // given
        val calendarTestSinceDate = Calendar.getInstance()
        calendarTestSinceDate.clear()
        calendarTestSinceDate.set(2016, Calendar.JUNE, 8)

        val workoutOneDate = calendarTestSinceDate.clone() as Calendar
        workoutOneDate.add(Calendar.YEAR, 1)

        val workoutTwoDate = calendarTestSinceDate.clone() as Calendar
        workoutTwoDate.add(Calendar.DAY_OF_MONTH, 1)

        val workoutThreeDate = calendarTestSinceDate.clone() as Calendar
        workoutThreeDate.add(Calendar.DAY_OF_MONTH, -1)

        val workout1 = Workout(WorkoutUoM.METERS, workoutOneDate.time, Date())
        val workout2 = Workout(WorkoutUoM.YARDS, workoutTwoDate.time, Date())
        val workout3 = Workout(WorkoutUoM.YARDS, workoutThreeDate.time, Date())

        val workoutOneID = dao.insert(workout1).test().values()[0]
        val workoutTwoID = dao.insert(workout2).test().values()[0]
        val workoutThreeID = dao.insert(workout3).test().values()[0]

        val workoutSetOne = WorkoutSet(0L, workoutOneID, 2, 300, "fly", Date(), Date())
        val workoutSetTwo = WorkoutSet(0L, workoutTwoID, 2, 400, "free", Date(), Date())
        val workoutSetThree = WorkoutSet(0L, workoutThreeID, 2, 400, "im", Date(), Date())

        dao.insert(listOf(workoutSetOne, workoutSetTwo, workoutSetThree)).test()

        // when
        val workoutsSinceDate = dao.getWorkoutSetsWithUoMSinceDate(calendarTestSinceDate.time.time).test()

        // then
        workoutsSinceDate.assertComplete()
        val workoutSetsSince = workoutsSinceDate.values()[0]
        assertEquals(2, workoutSetsSince.size)
        assertEquals(workoutSetOne.distance, workoutSetsSince[0].distance)
        assertEquals(workoutSetTwo.distance, workoutSetsSince[1].distance)
    }
}
