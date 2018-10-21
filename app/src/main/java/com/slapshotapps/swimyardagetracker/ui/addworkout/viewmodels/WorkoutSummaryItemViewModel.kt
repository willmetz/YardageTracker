package com.slapshotapps.swimyardagetracker.ui.addworkout.viewmodels

import com.slapshotapps.swimyardagetracker.models.workoutsets.WorkoutSet


class WorkoutSummaryItemViewModel(private val workoutSet: WorkoutSet, unitOfMeasure: String) {

    val stroke = workoutSet.stroke
    val uom = unitOfMeasure


    fun getReps(): String{
        return workoutSet.reps.toString()
    }

    fun getDistance(): String{
        return workoutSet.distance.toString()
    }
}