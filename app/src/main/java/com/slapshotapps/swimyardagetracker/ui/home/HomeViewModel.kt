package com.slapshotapps.swimyardagetracker.ui.home

import com.slapshotapps.swimyardagetracker.database.WorkoutDatabase
import com.slapshotapps.swimyardagetracker.models.workout.Workout
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutSet
import java.util.*
import javax.inject.Inject


class HomeViewModel @Inject constructor(private val workoutDatabase: WorkoutDatabase) {

    interface HomeViewModelListener {
        fun onAddWorkout()
    }

    private var listener: HomeViewModelListener? = null
    private var lastWorkout: Workout? = null
    private var lastWorkoutSets: List<WorkoutSet>? = null

    fun onResume() {
        lastWorkout = workoutDatabase.workoutDao().getLatestWorkout()

        val workout = lastWorkout
        if (workout != null) {
            lastWorkoutSets = workoutDatabase.workoutDao().getSetsForWorkout(workout.id)
        }
    }

    fun setListener(listener: HomeViewModelListener) {
        this.listener = listener
    }

    fun lastWorkoutYardage(): String {
        val lastRetrievedWorkout = lastWorkout
        val workoutSets = lastWorkoutSets;

        if (workoutSets == null || lastRetrievedWorkout == null) {
            return "N/A"
        }
        //add up workout distances
        var distance = 0
        workoutSets.forEach { distance += it.distance }

        return String.format(Locale.US, "%d %s", distance, lastRetrievedWorkout.uoM.toString())

    }

    fun lastWorkoutDate(): String {
        val lastRetrievedWorkout = lastWorkout

        if(lastRetrievedWorkout == null){
            return "N/A"
        }

        return lastRetrievedWorkout.getFormattedWorkoutDate()
    }

    fun totalWorkoutsForYear(): String {
        return "8"
    }

    fun totalYardageForYear(): String {
        return "5000"
    }

    fun onAddWorkoutTapped() {
        listener?.onAddWorkout()
    }
}
