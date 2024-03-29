package com.slapshotapps.swimyardagetracker.ui.addworkout.viewmodels

import com.slapshotapps.swimyardagetracker.managers.WorkoutManager
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class WorkoutDateViewModel @Inject constructor(private val workoutManager: WorkoutManager) {

    interface WorkoutDateViewModelListener {
        fun onShowDateSelection(currentWorkoutDate: Date)
        fun onSetWorkoutUoM()
    }

    private val dateFormatter = SimpleDateFormat("MMM d yyyy", Locale.US)

    fun getWorkoutDate() = workoutManager.workoutDate

    fun getFormattedWorkoutDate() = formatDate()

    fun onDateChanged(newDate: Date) {
        workoutManager.workoutDate = newDate
    }

    private fun formatDate(): String {
        return dateFormatter.format(workoutManager.workoutDate)
    }
}
