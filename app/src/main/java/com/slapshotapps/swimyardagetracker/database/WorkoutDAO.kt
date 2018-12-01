package com.slapshotapps.swimyardagetracker.database

import androidx.room.*
import com.slapshotapps.swimyardagetracker.models.workout.Workout
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutSet
import io.reactivex.Completable
import io.reactivex.Maybe


@Dao
interface WorkoutDAO {

    @Query("SELECT * FROM workouts ORDER BY workoutDate DESC LIMIT 1")
    fun getLatestWorkout(): Maybe<Workout>

    @Query("SELECT * FROM `workout-sets` WHERE workoutID=:workoutID")
    fun getSetsForWorkout(workoutID: Long): Maybe<List<WorkoutSet>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(workout: Workout): Maybe<Long>

    @Insert
    fun insert(workoutSets: List<WorkoutSet>): Completable

    @Update
    fun update(workout: Workout): Completable

    @Update
    fun update(workoutSet: WorkoutSet): Completable

    @Delete
    fun delete(workout: Workout): Completable

    @Delete
    fun delete(workoutSet: WorkoutSet): Completable
}