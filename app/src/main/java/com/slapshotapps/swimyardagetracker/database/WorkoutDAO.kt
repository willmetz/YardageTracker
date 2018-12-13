package com.slapshotapps.swimyardagetracker.database

import androidx.room.*
import com.slapshotapps.swimyardagetracker.models.workout.Workout
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutSet
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutWithUoM
import io.reactivex.Completable
import io.reactivex.Maybe


@Dao
interface WorkoutDAO {

    @Query("SELECT * FROM workouts ORDER BY workoutDate DESC LIMIT 1")
    fun getLatestWorkout(): Maybe<Workout>

    @Query("SELECT * FROM `workout-sets` WHERE workoutID=:workoutID")
    fun getSetsForWorkout(workoutID: Long): Maybe<List<WorkoutSet>>

    @Query("SELECT COUNT(`workoutDate`) FROM  workouts WHERE workoutDate > :fromTimeStamp")
    fun getWorkoutCountFromDate(fromTimeStamp: Long): Maybe<Int>

    @Query("SELECT w.uoM as uoM, w.workoutDate as workoutDate, s.reps as reps, s.distance as distance, s.stroke as stroke FROM workouts AS w INNER JOIN `workout-sets` as s ON w.id = s.workoutID WHERE workoutDate > :sinceTimeStamp")
    fun getWorkoutSetsWithUoMSinceDate(sinceTimeStamp: Long): Maybe<List<WorkoutWithUoM>>

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