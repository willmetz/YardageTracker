package com.slapshotapps.swimyardagetracker.database

import androidx.room.*
import com.slapshotapps.swimyardagetracker.models.workout.Workout
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutSet


@Dao
interface WorkoutDAO {

    @Query("SELECT * FROM workouts ORDER BY createDate DESC LIMIT 1")
    fun getLatestWorkout() : Workout?

    @Query("SELECT * FROM `workout-sets` WHERE workoutID=:workoutID")
    fun getSetsForWorkout(workoutID: Int) : List<WorkoutSet>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(workout: Workout) : Long

    @Insert
    fun insert(workoutSets: List<WorkoutSet>)

    @Update
    fun update(workout: Workout)

    @Update
    fun update(workoutSet: WorkoutSet)

    @Delete
    fun delete(workout: Workout)

    @Delete
    fun delete(workoutSet: WorkoutSet)
}