package com.slapshotapps.swimyardagetracker.ui.addworkout.viewmodels

import androidx.annotation.StringRes
import androidx.databinding.ObservableField
import com.slapshotapps.swimyardagetracker.R
import com.slapshotapps.swimyardagetracker.managers.WorkoutManager
import com.slapshotapps.swimyardagetracker.repositories.WorkoutRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class WorkoutSummaryViewModel @Inject constructor(private val workoutManager: WorkoutManager,
                                                  private val workoutRepository: WorkoutRepository) {

    interface WorkoutSummaryListener {
        fun onWorkoutDataReady(workoutSummaryItemViewModels: List<WorkoutSummaryItemViewModel>)
        fun onWorkAdded()
        fun onCancelAdd()
        fun onErrorAddingWorkout(@StringRes msgID: Int)
    }

    private var listener: WorkoutSummaryListener? = null
    private var disposables: CompositeDisposable? = null

    val workoutDateText = ObservableField<String>("")


    fun onViewReady() {
        disposables = CompositeDisposable()
        val data = workoutManager.getAllWorkoutSets()

        val viewModels = ArrayList<WorkoutSummaryItemViewModel>(data.size)

        data.forEach({
            viewModels.add(WorkoutSummaryItemViewModel(it, workoutManager.unitOfMeasure.toString()))
        })

        listener?.onWorkoutDataReady(viewModels)
    }

    fun onSubmitTapped() {
        //save the workout
        val disposable = workoutRepository.addWorkout(workoutManager.getWorkout(), workoutManager.getAllWorkoutSets())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    listener?.onWorkAdded()
                }, {
                    listener?.onErrorAddingWorkout(R.string.error_adding_workout)
                })

        disposables?.add(disposable)
    }

    fun onCancelTapped() {
        listener?.onCancelAdd()
    }
}