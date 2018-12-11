package com.slapshotapps.swimyardagetracker.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.slapshotapps.swimyardagetracker.models.workout.Workout
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutSet
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutWithUoM


@Database(entities = arrayOf(Workout::class, WorkoutSet::class), version = 1,
        views = arrayOf(WorkoutWithUoM::class))
@TypeConverters(DatabaseTypeConverters::class)
abstract class WorkoutDatabase: RoomDatabase() {

    abstract fun workoutDao(): WorkoutDAO
}