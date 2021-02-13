package com.slapshotapps.swimyardagetracker.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.slapshotapps.swimyardagetracker.database.WorkoutDatabase
import com.slapshotapps.swimyardagetracker.models.workout.Workout
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutSet
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutWithDetails
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutWithUoM
import io.reactivex.Completable
import io.reactivex.Maybe
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class WorkoutRepository @Inject constructor(private val workoutDatabase: WorkoutDatabase) {

    fun addWorkout(workout: Workout, workoutSets: List<WorkoutSet>): Completable {
        return workoutDatabase.workoutDao().insert(workout)
                .flatMapCompletable { workoutID ->
                    // need to set the workout ID to the newly inserted workout for each set
                    workoutSets.forEach({
                        it.workoutID = workoutID
                    })

                    workoutDatabase.workoutDao().insert(workoutSets)
                }
    }

    fun getMostRecentWorkoutWithDetails(): Maybe<WorkoutWithDetails> {
        lateinit var foundWorkout: Workout
        return workoutDatabase.workoutDao().getLatestWorkout()
                .flatMap { workout: Workout ->
                    foundWorkout = workout
                    workoutDatabase.workoutDao().getSetsForWorkout(workout.id)
                }
                .flatMap { workoutSets ->
                    Maybe.just(WorkoutWithDetails(foundWorkout, workoutSets))
                }
    }

    fun getWorkoutsWithDetails(): LiveData<List<WorkoutWithDetails>> {
        val workouts = workoutDatabase.workoutDao().getWorkouts()
        val workoutSets = workoutDatabase.workoutDao().getWorkoutSets()

        val allWorkouts = MediatorLiveData<List<WorkoutWithDetails>>()

        allWorkouts.addSource(workouts) {
            allWorkouts.value = buildAllWorkouts(workouts, workoutSets)
        }

        allWorkouts.addSource(workoutSets) {
            allWorkouts.value = buildAllWorkouts(workouts, workoutSets)
        }

        return allWorkouts
    }

    private fun buildAllWorkouts(workoutResult: LiveData<List<Workout>>, setResult: LiveData<List<WorkoutSet>>): List<WorkoutWithDetails> {
        val workouts = workoutResult.value
        val sets = setResult.value

        val allWorkoutInfo = ArrayList<WorkoutWithDetails>()

        if (workouts == null || sets == null) {
            return allWorkoutInfo
        }

        for (workout: Workout in workouts) {
            allWorkoutInfo.add(WorkoutWithDetails(workout, sets.filter { set -> set.workoutID == workout.id }))
        }

        return allWorkoutInfo
    }

    fun getWorkoutsCountSinceDate(fromDate: Date): Maybe<Int> {
        return workoutDatabase.workoutDao().getWorkoutCountFromDate(fromDate.time)
    }

    fun getAllWorkoutsWithUoMSinceDate(since: Date): Maybe<List<WorkoutWithUoM>> {
        return workoutDatabase.workoutDao().getWorkoutSetsWithUoMSinceDate(since.time)
    }
}
