package com.slapshotapps.swimyardagetracker.ui.addworkout.viewmodels

import androidx.databinding.ObservableField
import com.slapshotapps.swimyardagetracker.managers.WorkoutManager
import javax.inject.Inject


class WorkoutSummaryViewModel @Inject constructor(private val workoutManager: WorkoutManager) {

    interface WorkoutSummaryListener {
        fun onWorkoutDataReady(workoutSummaryItemViewModels: List<WorkoutSummaryItemViewModel>)
    }

    var listener: WorkoutSummaryListener? = null

    val workoutDateText = ObservableField<String>("")


    fun onViewReady() {
        val data = workoutManager.getAllWorkoutSets()

        val viewModels = ArrayList<WorkoutSummaryItemViewModel>(data.size)

        data.forEach({
            viewModels.add(WorkoutSummaryItemViewModel(it, workoutManager.unitOfMeasure.toString()))
        })

        listener?.onWorkoutDataReady(viewModels)
    }
}