package com.slapshotapps.swimyardagetracker.ui.addworkout.viewmodels

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.slapshotapps.swimyardagetracker.R
import com.slapshotapps.swimyardagetracker.managers.WorkoutManager
import com.slapshotapps.swimyardagetracker.models.workout.Workout
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutSet
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutUoM
import com.slapshotapps.swimyardagetracker.repositories.WorkoutRepository
import io.reactivex.Completable
import java.util.Date
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rules.ImmediateSchedulersRule

class WorkoutSummaryViewModelTest {

    @get:Rule
    val immediateSchedulersRule = ImmediateSchedulersRule()

    lateinit var workoutManager: WorkoutManager
    lateinit var workoutRepository: WorkoutRepository

    @Before
    fun setUp() {

        workoutManager = mock()
        workoutRepository = mock()
    }

    @Test
    fun test_onWorkoutAddFailure() {

        // given
        val countDownLatch = CountDownLatch(1)
        whenever(workoutManager.getWorkout()).thenReturn(Workout(WorkoutUoM.YARDS, Date(), Date()))
        whenever(workoutManager.getAllWorkoutSets()).thenReturn(ArrayList<WorkoutSet>())

        val viewModel = WorkoutSummaryViewModel(workoutManager, workoutRepository)

        whenever(workoutRepository.addWorkout(any(), any())).thenReturn(Completable.error(Throwable()))

        viewModel.listener = object : WorkoutSummaryHelper() {
            override fun onErrorAddingWorkout(msgID: Int) {
                assert(msgID == R.string.error_adding_workout)
                countDownLatch.countDown()
            }
        }

        // when
        viewModel.onSubmitTapped()

        // then
        assert(countDownLatch.await(20, TimeUnit.MILLISECONDS))
    }

    @Test
    fun test_onWorkoutAddSuccess() {
        // given
        val countDownLatch = CountDownLatch(1)
        whenever(workoutManager.getWorkout()).thenReturn(Workout(WorkoutUoM.YARDS, Date(), Date()))
        whenever(workoutManager.getAllWorkoutSets()).thenReturn(ArrayList<WorkoutSet>())

        val viewModel = WorkoutSummaryViewModel(workoutManager, workoutRepository)

        whenever(workoutRepository.addWorkout(any(), any())).thenReturn(Completable.complete())

        viewModel.listener = object : WorkoutSummaryHelper() {
            override fun onWorkAdded() {
                countDownLatch.countDown()
            }
        }

        // when
        viewModel.onSubmitTapped()

        // then
        assert(countDownLatch.await(20, TimeUnit.MILLISECONDS))
    }

    open class WorkoutSummaryHelper : WorkoutSummaryViewModel.WorkoutSummaryListener {
        override fun onWorkoutDataReady(workoutSummaryItemViewModels: List<WorkoutSummaryItemViewModel>) {
        }

        override fun onWorkAdded() {
        }

        override fun onCancelAdd() {
        }

        override fun onErrorAddingWorkout(msgID: Int) {
        }
    }
}
