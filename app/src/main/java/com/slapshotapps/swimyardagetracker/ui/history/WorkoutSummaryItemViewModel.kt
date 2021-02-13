package com.slapshotapps.swimyardagetracker.ui.history

import com.slapshotapps.swimyardagetracker.models.workout.WorkoutSet
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutWithDetails

class WorkoutSummaryItemViewModel(val workout: WorkoutWithDetails) {

    var totalDistanceWithUoM: String = ""
        get() {
            var totalDistance = 0
            for (workoutSet: WorkoutSet in workout.workoutSets) {
                totalDistance += workoutSet.distance * workoutSet.reps
            }

            return "$totalDistance ${workout.workout.uoM.label}"
        }
        private set

    var workoutDate: String = ""
        get() {
            return workout.workout.getFormattedWorkoutDate()
        }
        private set

    var workoutStrokes: String = ""
        get() {
            var strokes = ""
            for (workoutSet: WorkoutSet in workout.workoutSets) {
                when {
                    strokes.isBlank() -> strokes = workoutSet.stroke
                    !strokes.contains(workoutSet.stroke) -> strokes = "$strokes, ${workoutSet.stroke}"
                }
            }

            return strokes
        }
        private set
}
