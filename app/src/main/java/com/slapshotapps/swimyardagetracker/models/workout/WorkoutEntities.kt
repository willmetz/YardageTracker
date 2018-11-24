package com.slapshotapps.swimyardagetracker.models.workout

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName = "workouts", indices = arrayOf(Index("createDate")))
data class Workout(@PrimaryKey(autoGenerate = true) val id: Int,
                   val uoM: WorkoutUoM,
                   val workoutSets: List<WorkoutSet>,
                   val createDate: Date,
                   val updateDate: Date){

}

@Entity(tableName = "workout-sets", foreignKeys = arrayOf(ForeignKey(
        entity = Workout::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("workoutID"),
        onDelete = ForeignKey.CASCADE)))
data class WorkoutSet(@PrimaryKey(autoGenerate = true)val id: Int,
                      val workoutID: Int,
                      val reps: Int,
                      val distance: Int,
                      val stroke: String,
                      val createDate: Date,
                      val updateDate: Date) {
}
