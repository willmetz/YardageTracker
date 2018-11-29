package com.slapshotapps.swimyardagetracker.ui.addworkout.viewmodels

import androidx.databinding.ObservableField
import com.slapshotapps.swimyardagetracker.database.WorkoutDatabase
import com.slapshotapps.swimyardagetracker.managers.WorkoutManager
import javax.inject.Inject


class WorkoutSummaryViewModel @Inject constructor(private val workoutManager: WorkoutManager,
                                                  private val workoutDatabase: WorkoutDatabase) {

    interface WorkoutSummaryListener {
        fun onWorkoutDataReady(workoutSummaryItemViewModels: List<WorkoutSummaryItemViewModel>)
        fun onWorkAdded()
        fun onCancelAdd()
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

    fun onSubmitTapped() {
        //save the workout to the DB
        val workoutID = workoutDatabase.workoutDao().insert(workoutManager.getWorkout())

        val workoutSets = workoutManager.getAllWorkoutSets()

        //need to set the workout ID to the newly inserted workout for each set
        workoutSets.forEach({
            it.workoutID = workoutID
        })

        workoutDatabase.workoutDao().insert(workoutSets)

        listener?.onWorkAdded()
    }

    fun onCancelTapped() {
        listener?.onCancelAdd()
    }
}