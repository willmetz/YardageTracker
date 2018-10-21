package com.slapshotapps.swimyardagetracker.ui.addworkout.viewmodels

import androidx.databinding.ObservableField
import com.slapshotapps.swimyardagetracker.managers.WorkoutManager
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class WorkoutDateViewModel @Inject constructor(private val workoutManager: WorkoutManager) {

    interface WorkoutDateViewModelListener {
        fun onShowDateSelection(currentWorkoutDate: Date)
        fun onSetWorkoutUoM()
    }

    private var listener: WorkoutDateViewModelListener? = null
    private val dateFormatter = SimpleDateFormat("MMM d yyyy", Locale.US)
    val formattedWorkoutDate = ObservableField<String>(formatDate())


    fun setListener(listener: WorkoutDateViewModelListener) {
        this.listener = listener
    }

    fun onDateTapped() {
        listener?.onShowDateSelection(workoutManager.workoutDate)
    }

    fun onAddWorkoutInfoTapped() {
        listener?.onSetWorkoutUoM()
    }

    fun onDateChanged(newDate: Date) {
        workoutManager.workoutDate = newDate
        formattedWorkoutDate.set(formatDate())
    }

    private fun formatDate(): String{
        return dateFormatter.format(workoutManager.workoutDate)
    }

}