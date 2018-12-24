package com.slapshotapps.swimyardagetracker.ui.addworkout.viewmodels

import com.nhaarman.mockitokotlin2.mock
import com.slapshotapps.swimyardagetracker.managers.WorkoutManager
import com.slapshotapps.swimyardagetracker.repositories.WorkoutRepository
import org.junit.Before
import org.junit.Rule
import rules.ImmediateSchedulersRule

class WorkoutSummaryViewModelTest {

    @Rule
    val immediateSchedulersRule = ImmediateSchedulersRule()

    lateinit var workoutManager: WorkoutManager
    lateinit var workoutRepository: WorkoutRepository

    @Before
    fun setUp() {

        workoutManager = mock()
        workoutRepository = mock()
    }
}