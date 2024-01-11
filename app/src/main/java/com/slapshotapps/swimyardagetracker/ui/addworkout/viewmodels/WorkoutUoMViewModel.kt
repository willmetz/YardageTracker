package com.slapshotapps.swimyardagetracker.ui.addworkout.viewmodels

import com.slapshotapps.swimyardagetracker.managers.WorkoutManager
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutUoM
import javax.inject.Inject

class WorkoutUoMViewModel @Inject constructor(private val workoutManager: WorkoutManager) {

    val uomEntries = listOf("Yards", "Meters")
    var uomValue = "Yards"

    fun onNext() {
        if (uomValue.equals("yards", true)) {
            workoutManager.unitOfMeasure = WorkoutUoM.YARDS
        } else {
            workoutManager.unitOfMeasure = WorkoutUoM.METERS
        }
    }
}
