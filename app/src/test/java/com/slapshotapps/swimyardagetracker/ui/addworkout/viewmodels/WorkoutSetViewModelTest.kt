package com.slapshotapps.swimyardagetracker.ui.addworkout.viewmodels

import com.nhaarman.mockitokotlin2.mock
import com.slapshotapps.swimyardagetracker.R
import com.slapshotapps.swimyardagetracker.managers.WorkoutManager
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class WorkoutSetViewModelTest {

    lateinit var workoutManager: WorkoutManager

    @Before
    fun setUp() {

        workoutManager = mock()
    }

    @Test
    fun test_addSetSelectedWithNoRepsSetResultsInErrorMessage() {

        //given
        val countDownLatch = CountDownLatch(1)
        val viewModel = WorkoutSetViewModel(workoutManager)
        viewModel.listener = object : WorkoutSetViewModelListenerHelper() {
            override fun onRepsEntryError(resID: Int) {
                assert(resID == R.string.rep_invalid_entry)
                countDownLatch.countDown()
            }
        }

        viewModel.setStroke.set("Free")
        viewModel.setDistance.set("100")

        //when
        viewModel.onAddAnotherSetTapped()

        //then
        assert(countDownLatch.await(50, TimeUnit.MILLISECONDS))
    }


    open class WorkoutSetViewModelListenerHelper() : WorkoutSetViewModel.WorkoutSetViewModelListener {
        override fun onRepsEntryError(resID: Int) {

        }

        override fun onDistanceEntryError(resID: Int) {

        }

        override fun onStrokeEntryError(resID: Int) {

        }

        override fun onShowNoSetErrorMsg(resID: Int) {

        }

        override fun onValidSetAdded() {

        }

        override fun onShowWorkoutSummary() {

        }
    }
}