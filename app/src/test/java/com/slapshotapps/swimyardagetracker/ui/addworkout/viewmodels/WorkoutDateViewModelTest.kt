package com.slapshotapps.swimyardagetracker.ui.addworkout.viewmodels

import com.slapshotapps.swimyardagetracker.managers.WorkoutManager
import org.junit.Before
import org.junit.Test
import java.util.*

class WorkoutDateViewModelTest {

    lateinit var workoutManager: WorkoutManager

    @Before
    fun setUp() {

        workoutManager = WorkoutManager()
    }


    @Test
    fun test_workoutDateSavedOnChange(){
        val date = Date()

        val viewModel = WorkoutDateViewModel(workoutManager)

        viewModel.onDateChanged(date)

        assert(workoutManager.workoutDate.compareTo(date) == 0)
    }
}