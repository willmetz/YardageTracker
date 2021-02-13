package com.slapshotapps.swimyardagetracker.ui.addworkout.viewmodels

import com.slapshotapps.swimyardagetracker.managers.WorkoutManager
import java.util.Date
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class WorkoutDateViewModelTest {

    lateinit var workoutManager: WorkoutManager

    @Before
    fun setUp() {

        workoutManager = WorkoutManager()
    }

    @Test
    fun test_workoutDateSavedOnChange() {
        val date = Date()

        val viewModel = WorkoutDateViewModel(workoutManager)

        viewModel.onDateChanged(date)

        assertEquals(0, workoutManager.workoutDate.compareTo(date))
    }
}
