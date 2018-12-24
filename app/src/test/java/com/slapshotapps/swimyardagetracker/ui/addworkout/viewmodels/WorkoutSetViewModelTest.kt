package com.slapshotapps.swimyardagetracker.ui.addworkout.viewmodels

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.slapshotapps.swimyardagetracker.R
import com.slapshotapps.swimyardagetracker.managers.WorkoutManager
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutSet
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

    @Test
    fun test_addSetSelectedWithNoDistanceSetResultsInErrorMessage() {

        //given
        val countDownLatch = CountDownLatch(1)
        val viewModel = WorkoutSetViewModel(workoutManager)
        viewModel.listener = object : WorkoutSetViewModelListenerHelper() {
            override fun onDistanceEntryError(resID: Int) {
                assert(resID == R.string.distance_invalid_entry)
                countDownLatch.countDown()
            }
        }

        viewModel.setStroke.set("Free")
        viewModel.setReps.set("2")

        //when
        viewModel.onAddAnotherSetTapped()

        //then
        assert(countDownLatch.await(50, TimeUnit.MILLISECONDS))
    }

    @Test
    fun test_addSetSelectedWithNoStrokeSetResultsInErrorMessage() {

        //given
        val countDownLatch = CountDownLatch(1)
        val viewModel = WorkoutSetViewModel(workoutManager)
        viewModel.listener = object : WorkoutSetViewModelListenerHelper() {
            override fun onStrokeEntryError(resID: Int) {
                assert(resID == R.string.stroke_invalid_entry)
                countDownLatch.countDown()
            }
        }

        viewModel.setDistance.set("100")
        viewModel.setReps.set("2")

        //when
        viewModel.onAddAnotherSetTapped()

        //then
        assert(countDownLatch.await(50, TimeUnit.MILLISECONDS))
    }

    @Test
    fun test_goToNextStepWithNoSetsResultsInErrorMessage() {

        //given
        val countDownLatch = CountDownLatch(1)
        val viewModel = WorkoutSetViewModel(workoutManager)
        viewModel.listener = object : WorkoutSetViewModelListenerHelper() {
            override fun onShowNoSetErrorMsg(resID: Int) {
                assert(resID == R.string.no_data_entered_msg)
                countDownLatch.countDown()
            }
        }

        //when
        viewModel.onNextTapped()

        //then
        assert(countDownLatch.await(50, TimeUnit.MILLISECONDS))
    }

    @Test
    fun test_addValidSetClearsInputs() {

        //given
        val viewModel = WorkoutSetViewModel(workoutManager)
        viewModel.setDistance.set("100")
        viewModel.setReps.set("2")
        viewModel.setStroke.set("Free")

        //when
        viewModel.onAddAnotherSetTapped()

        //then
        assert(viewModel.setDistance.get() == "")
        assert(viewModel.setReps.get() == "")
        assert(viewModel.setStroke.get() == "")
    }

    @Test
    fun test_addMultipleSetsAndDoneWorks() {

        //given
        val viewModel = WorkoutSetViewModel(workoutManager)
        viewModel.setDistance.set("100")
        viewModel.setReps.set("2")
        viewModel.setStroke.set("Free")
        viewModel.onAddAnotherSetTapped()

        viewModel.setDistance.set("200")
        viewModel.setReps.set("3")
        viewModel.setStroke.set("Fly")

        //when
        viewModel.onNextTapped()

        //then
        argumentCaptor<WorkoutSet>().apply {

            verify(workoutManager, times(2)).addWorkoutSet(capture())

            assert(allValues[0].stroke == "Free")
            assert(allValues[1].stroke == "Fly")
        }

        //set info is cleared
        assert(viewModel.setDistance.get() == "")
        assert(viewModel.setReps.get() == "")
        assert(viewModel.setStroke.get() == "")
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