package com.slapshotapps.swimyardagetracker.repositories

import com.slapshotapps.swimyardagetracker.database.WorkoutDatabase
import com.slapshotapps.swimyardagetracker.models.workout.Workout
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutSet
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class WorkoutRepository @Inject constructor(private val workoutDatabase: WorkoutDatabase) {

    fun addWorkout(workout: Workout, workoutSets: List<WorkoutSet>): Completable{
        return workoutDatabase.workoutDao().insert(workout)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapCompletable {
                    val workoutID = it

                    //need to set the workout ID to the newly inserted workout for each set
                    workoutSets.forEach({
                        it.workoutID = workoutID
                    })

                    workoutDatabase.workoutDao().insert(workoutSets)
                }
    }
}