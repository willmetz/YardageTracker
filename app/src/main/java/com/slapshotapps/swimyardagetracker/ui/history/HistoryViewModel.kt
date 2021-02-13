package com.slapshotapps.swimyardagetracker.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutWithDetails
import com.slapshotapps.swimyardagetracker.repositories.WorkoutRepository
import javax.inject.Inject

class HistoryViewModel @Inject constructor(private val workoutRepository: WorkoutRepository) {

    val allWorkouts: LiveData<ArrayList<WorkoutSummaryItemViewModel>> by lazy {
        workoutRepository.getWorkoutsWithDetails().map {

            val workoutList = ArrayList<WorkoutSummaryItemViewModel>()

            for (workoutsWithDetails: WorkoutWithDetails in it) {
                workoutList.add(WorkoutSummaryItemViewModel(workoutsWithDetails))
            }

            if (workoutList.isNotEmpty()) {
                workoutList.sortWith(Comparator { w1, w2 ->
                    when {
                        w1.workout.workout.workoutDate > w2.workout.workout.workoutDate -> -1
                        w1.workout.workout.workoutDate == w2.workout.workout.workoutDate -> 0
                        else -> 1
                    }
                })
            }

            workoutList
        }
    }
}
