package com.slapshotapps.swimyardagetracker.ui.addworkout.viewmodels

import com.slapshotapps.swimyardagetracker.R
import com.slapshotapps.swimyardagetracker.managers.WorkoutManager
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutSet
import java.util.Date
import javax.inject.Inject

class WorkoutSetViewModel @Inject constructor(private val workoutManager: WorkoutManager) {

    interface WorkoutSetViewModelListener {
        fun onRepsEntryError(resID: Int)
        fun onDistanceEntryError(resID: Int)
        fun onStrokeEntryError(resID: Int)
        fun onShowNoSetErrorMsg(resID: Int)
        fun onValidSetAdded()
        fun onShowWorkoutSummary()
    }

    var listener: WorkoutSetViewModelListener? = null

    private val workoutSets = ArrayList<WorkoutSet>()

    fun onNextTapped(reps: String, distance: String, stroke: String): Boolean {
        if ((reps.toIntOrNull() ?: -1) > 0 && (distance.toIntOrNull() ?: -1) > 0 && stroke.isNotBlank()) {
            val set = WorkoutSet(reps.toIntOrNull() ?: 0, distance.toIntOrNull() ?: 0, stroke, Date())
            workoutSets.add(set)
        }

        if (workoutSets.isEmpty()) {
            listener?.onShowNoSetErrorMsg(R.string.no_data_entered_msg)
            return false
        }

        workoutManager.clearWorkoutSets()
        workoutSets.forEach {
            workoutManager.addWorkoutSet(it)
        }

        listener?.onShowWorkoutSummary()

        return true
    }

    fun addAnotherSet(reps: String, distance: String, stroke: String) {
        val workout = getSetInfo(reps, distance, stroke) ?: return

        workoutSets.add(workout)

        listener?.onValidSetAdded()
    }

    private fun getSetInfo(reps: String, distance: String, stroke: String): WorkoutSet? {

        if ((reps.toIntOrNull() ?: -1) < 0) {
            listener?.onRepsEntryError(R.string.rep_invalid_entry)
            return null
        }

        if ((distance.toIntOrNull() ?: -1) < 0) {
            listener?.onDistanceEntryError(R.string.distance_invalid_entry)
            return null
        }

        if (stroke.isBlank()) {
            listener?.onStrokeEntryError(R.string.stroke_invalid_entry)
            return null
        }

        return WorkoutSet(reps.toIntOrNull() ?: 0, distance.toIntOrNull() ?: 0, stroke, Date())
    }
}
