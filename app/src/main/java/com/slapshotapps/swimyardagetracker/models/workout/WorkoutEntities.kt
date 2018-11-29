package com.slapshotapps.swimyardagetracker.models.workout

import androidx.room.*
import java.util.*


@Entity(tableName = "workouts", indices = arrayOf(Index("createDate")))
data class Workout(@PrimaryKey(autoGenerate = true) val id: Int,
                   val uoM: WorkoutUoM,
                   val createDate: Date,
                   val updateDate: Date){

}

@Entity(tableName = "workout-sets", foreignKeys = arrayOf(ForeignKey(
        entity = Workout::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("workoutID"),
        onDelete = ForeignKey.CASCADE)), indices = arrayOf(Index("workoutID")))
data class WorkoutSet(@PrimaryKey(autoGenerate = true)val id: Int,
                      val workoutID: Int,
                      val reps: Int,
                      val distance: Int,
                      val stroke: String,
                      val createDate: Date,
                      val updateDate: Date) {

    @Ignore
    constructor(reps: Int,
                distance: Int,
                stroke: String,
                createDate: Date):this(0,1,reps, distance,stroke,createDate,createDate)
}
