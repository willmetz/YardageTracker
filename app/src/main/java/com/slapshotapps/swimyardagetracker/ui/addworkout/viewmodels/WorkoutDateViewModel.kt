package com.slapshotapps.swimyardagetracker.ui.addworkout.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*


class WorkoutDateViewModel() : ViewModel() {

    interface WorkoutDateViewModelListener {
        fun onShowDateSelection(currentWorkoutDate: Date)
        fun onAddWorkoutDetails()
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
        listener?.onAddWorkoutDetails()
    }

    fun onDateChanged(newDate: Date) {
        workoutDate.time = newDate.time
        formattedWorkoutDate.set(formatDate())
    }

    private fun formatDate(): String {
        return dateFormatter.format(workoutDate)
    }
}