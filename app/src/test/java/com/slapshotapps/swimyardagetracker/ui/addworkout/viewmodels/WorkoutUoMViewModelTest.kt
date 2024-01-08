package com.slapshotapps.swimyardagetracker.ui.addworkout.viewmodels

import com.slapshotapps.swimyardagetracker.managers.WorkoutManager
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutUoM
import org.junit.Before
import org.junit.Test

class WorkoutUoMViewModelTest {

    lateinit var workoutManager: WorkoutManager

    @Before
    fun setUp() {

        workoutManager = WorkoutManager()
    }

    @Test
    fun test_unitOfMeasureYardsSelected() {
        val viewModel = WorkoutUoMViewModel(workoutManager)

        viewModel.uomValue = WorkoutUoM.YARDS.toString()

        viewModel.onNextTapped()

        assert(workoutManager.unitOfMeasure == WorkoutUoM.YARDS)
    }

    @Test
    fun test_unitOfMeasureMetersSelected() {
        val viewModel = WorkoutUoMViewModel(workoutManager)

        viewModel.uomValue = WorkoutUoM.METERS.toString()

        viewModel.onNextTapped()

        assert(workoutManager.unitOfMeasure == WorkoutUoM.METERS)
    }
}
