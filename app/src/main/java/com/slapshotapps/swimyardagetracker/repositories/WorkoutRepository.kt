package com.slapshotapps.swimyardagetracker.repositories

import com.slapshotapps.swimyardagetracker.database.WorkoutDatabase
import com.slapshotapps.swimyardagetracker.models.workout.Workout
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutSet
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutWithDetails
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutWithUoM
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject


class WorkoutRepository @Inject constructor(private val workoutDatabase: WorkoutDatabase) {

    fun addWorkout(workout: Workout, workoutSets: List<WorkoutSet>): Completable {
        return workoutDatabase.workoutDao().insert(workout)
                .flatMapCompletable { workoutID ->
                    //need to set the workout ID to the newly inserted workout for each set
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

    fun getAllWorkoutsWithDetails(): Maybe<List<WorkoutWithDetails>> {

        val getAllWorkoutsRequest = workoutDatabase.workoutDao().getAllWorkouts().subscribeOn(Schedulers.io())

        val getAllWorkoutSetsRequest = workoutDatabase.workoutDao().getAllWorkoutSets().subscribeOn(Schedulers.io())

        return Maybe.zip(getAllWorkoutsRequest, getAllWorkoutSetsRequest, BiFunction { allWorkouts, allWorkoutSets ->

            val workoutsWithDetails = ArrayList<WorkoutWithDetails>()

            for (workout: Workout in allWorkouts) {
                workoutsWithDetails.add(WorkoutWithDetails(workout, allWorkoutSets.filter {set -> set.workoutID == workout.id }))
            }

            workoutsWithDetails
        });
    }

    fun getWorkoutsCountSinceDate(fromDate: Date): Maybe<Int> {
        return workoutDatabase.workoutDao().getWorkoutCountFromDate(fromDate.time)
    }

    fun getAllWorkoutsWithUoMSinceDate(since: Date): Maybe<List<WorkoutWithUoM>> {
        return workoutDatabase.workoutDao().getWorkoutSetsWithUoMSinceDate(since.time)
    }
}