package com.slapshotapps.swimyardagetracker.models.workout

import androidx.room.*
import java.util.*


@Entity(tableName = "workouts", indices = arrayOf(Index("workoutDate")))
data class Workout(@PrimaryKey(autoGenerate = true) var id: Long,
                   val uoM: WorkoutUoM,
                   val workoutDate: Date,
                   val updateDate: Date){

    @Ignore
    constructor(unitOfMeasure: WorkoutUoM, workoutDate: Date, updateDate: Date):this(0, unitOfMeasure, workoutDate, updateDate)

}

@Entity(tableName = "workout-sets", foreignKeys = arrayOf(ForeignKey(
        entity = Workout::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("workoutID"),
        onDelete = ForeignKey.CASCADE)), indices = arrayOf(Index("workoutID")))
data class WorkoutSet(@PrimaryKey(autoGenerate = true)val id: Long,
                      var workoutID: Long,
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
