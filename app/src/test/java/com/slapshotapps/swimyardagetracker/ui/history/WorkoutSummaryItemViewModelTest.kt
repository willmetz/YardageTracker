package com.slapshotapps.swimyardagetracker.ui.history

import com.slapshotapps.swimyardagetracker.models.workout.Workout
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutSet
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutUoM
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutWithDetails
import java.util.Date
import kotlin.collections.ArrayList
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class WorkoutSummaryItemViewModelTest {

    lateinit var viewModel: WorkoutSummaryItemViewModel

    @Before
    fun setUp() {
    }

    @Test
    fun testDistanceAndStrokesOnWorkoutSet() {
        val workout = Workout(1L, WorkoutUoM.YARDS, Date(), Date())
        val workoutSets = ArrayList<WorkoutSet>()

        val setOne = WorkoutSet(10, 100, "fly", Date())
        val setTwo = WorkoutSet(5, 50, "free", Date())
        val setThree = WorkoutSet(8, 75, "back", Date())

        workoutSets.add(setOne)
        workoutSets.add(setTwo)
        workoutSets.add(setThree)

        val viewModel = WorkoutSummaryItemViewModel(WorkoutWithDetails(workout, workoutSets))

        Assert.assertEquals("1850 Yards", viewModel.totalDistanceWithUoM)
        Assert.assertEquals("fly, free, back", viewModel.workoutStrokes)
    }

    @Test
    fun testDistanceInMetersStrokesOnWorkoutSet() {
        val workout = Workout(1L, WorkoutUoM.METERS, Date(), Date())
        val workoutSets = ArrayList<WorkoutSet>()

        val setOne = WorkoutSet(10, 100, "fly", Date())
        val setTwo = WorkoutSet(5, 50, "free", Date())
        val setThree = WorkoutSet(8, 75, "back", Date())
        val setFour = WorkoutSet(2, 100, "fly", Date())

        workoutSets.add(setOne)
        workoutSets.add(setTwo)
        workoutSets.add(setThree)
        workoutSets.add(setFour)

        val viewModel = WorkoutSummaryItemViewModel(WorkoutWithDetails(workout, workoutSets))

        Assert.assertEquals("2050 Meters", viewModel.totalDistanceWithUoM)
        Assert.assertEquals("fly, free, back", viewModel.workoutStrokes)
    }
}
