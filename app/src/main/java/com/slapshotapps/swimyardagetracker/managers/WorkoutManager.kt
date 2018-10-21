package com.slapshotapps.swimyardagetracker.managers

import com.slapshotapps.swimyardagetracker.models.workoutsets.WorkoutSet
import com.slapshotapps.swimyardagetracker.models.workoutsets.WorkoutUoM
import java.util.*


class WorkoutManager {

    private val workoutSets = ArrayList<WorkoutSet>()

    var unitOfMeasure = WorkoutUoM.YARDS

    var workoutDate = Date()

    fun addWorkoutSet(workoutSet: WorkoutSet){
        workoutSets.add(workoutSet)
    }

    fun clearWorkoutSets(){
        workoutSets.clear()
    }

    fun getAllWorkoutSets(): List<WorkoutSet>{
        return workoutSets
    }
}