package com.slapshotapps.swimyardagetracker.ui.addworkout.viewmodels

import androidx.databinding.ObservableField
import com.slapshotapps.swimyardagetracker.managers.WorkoutManager
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class WorkoutDateViewModel @Inject constructor(workoutManager: WorkoutManager) {

    interface WorkoutDateViewModelListener {
        fun onShowDateSelection(currentWorkoutDate: Date)
        fun onSetWorkoutUoM()
    }

    private var listener: WorkoutDateViewModelListener? = null
    private val dateFormatter = SimpleDateFormat("MMM d yyyy", Locale.US)
    private val workoutDate = Date()
    val formattedWorkoutDate = ObservableField<String>(formatDate())


    fun setListener(listener: WorkoutDateViewModelListener) {
        this.listener = listener
    }

    fun onDateTapped() {
        listener?.onShowDateSelection(workoutDate)
    }

    fun onAddWorkoutInfoTapped() {
        listener?.onSetWorkoutUoM()
    }

    fun onDateChanged(newDate: Date) {
        workoutDate.time = newDate.time
        formattedWorkoutDate.set(formatDate())
    }

    private fun formatDate(): String {
        return dateFormatter.format(workoutDate)
    }
}