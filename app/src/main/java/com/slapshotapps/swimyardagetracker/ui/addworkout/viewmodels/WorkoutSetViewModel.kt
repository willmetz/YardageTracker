package com.slapshotapps.swimyardagetracker.ui.addworkout.viewmodels

import androidx.databinding.ObservableField
import com.slapshotapps.swimyardagetracker.R
import com.slapshotapps.swimyardagetracker.managers.WorkoutManager
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutSet
import com.slapshotapps.swimyardagetracker.utils.KeyboardActionButtonListener
import java.util.*
import javax.inject.Inject


class WorkoutSetViewModel @Inject constructor(private val workoutManager: WorkoutManager) : KeyboardActionButtonListener {


    interface WorkoutSetViewModelListener {
        fun onRepsEntryError(resID: Int)
        fun onDistanceEntryError(resID: Int)
        fun onStrokeEntryError(resID: Int)
        fun onShowNoSetErrorMsg(resID: Int)
        fun onValidSetAdded()
        fun onShowWorkoutSummary()
    }

    val setReps = ObservableField<String>("")
    val setDistance = ObservableField<String>("")
    val setStroke = ObservableField<String>("")

    var listener: WorkoutSetViewModelListener? = null

    private val workoutSets = ArrayList<WorkoutSet>()


    fun onAddAnotherSetTapped() {
        val workout = getSetInfo()

        if (workout == null) {
            return
        }

        workoutSets.add(workout)

        listener?.onValidSetAdded()

        clearSetInfo()
    }

    fun onNextTapped() {
        addAnotherSet()
    }

    override fun onDoneSelected() {
        //add the set
        addAnotherSet()
    }

    private fun addAnotherSet() {
        val workout = getSetInfo()

        clearSetInfo()

        if (workout != null) {
            workoutSets.add(workout)
        }

        if (workoutSets.isEmpty()) {
            listener?.onShowNoSetErrorMsg(R.string.no_data_entered_msg)
            return
        }

        workoutManager.clearWorkoutSets()
        workoutSets.forEach {
            workoutManager.addWorkoutSet(it)
        }

        listener?.onShowWorkoutSummary()
    }

    private fun getSetInfo(): WorkoutSet? {
        val reps = setReps.get()?.toIntOrNull() ?: 0
        val distance = setDistance.get()?.toIntOrNull() ?: 0
        val stroke = setStroke.get() ?: ""

        if (reps <= 0) {
            listener?.onRepsEntryError(R.string.rep_invalid_entry)
            return null
        }

        if (distance <= 0) {
            listener?.onDistanceEntryError(R.string.distance_invalid_entry)
            return null
        }

        if (stroke.isEmpty()) {
            listener?.onStrokeEntryError(R.string.stroke_invalid_entry)
            return null
        }

        return WorkoutSet(reps, distance, stroke, Date())
    }

    private fun clearSetInfo() {
        setDistance.set("")
        setReps.set("")
        setStroke.set("")
    }
}