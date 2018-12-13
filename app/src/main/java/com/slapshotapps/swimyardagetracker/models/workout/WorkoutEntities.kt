package com.slapshotapps.swimyardagetracker.models.workout

import androidx.room.*
import java.text.SimpleDateFormat
import java.util.*


@Entity(tableName = "workouts", indices = arrayOf(Index("workoutDate")))
data class Workout(@PrimaryKey(autoGenerate = true) var id: Long,
                   val uoM: WorkoutUoM,
                   val workoutDate: Date,
                   val updateDate: Date) {

    @Ignore
    private val workoutDateFormatter = SimpleDateFormat("EEE MMMM d yyyy", Locale.US)

    @Ignore
    constructor(unitOfMeasure: WorkoutUoM, workoutDate: Date, updateDate: Date) : this(0, unitOfMeasure, workoutDate, updateDate)

    @Ignore
    fun getFormattedWorkoutDate(): String {
        return workoutDateFormatter.format(workoutDate)
    }

}

@Entity(tableName = "workout-sets", foreignKeys = arrayOf(ForeignKey(
        entity = Workout::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("workoutID"),
        onDelete = ForeignKey.CASCADE)), indices = arrayOf(Index("workoutID")))
data class WorkoutSet(@PrimaryKey(autoGenerate = true) val id: Long,
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
                createDate: Date) : this(0, 1, reps, distance, stroke, createDate, createDate)
}


data class WorkoutWithDetails(val workout: Workout, val workoutSets: List<WorkoutSet>)

//
//@DatabaseView("SELECT w.uoM, w.workoutDate, s.reps, s.distance, s.stroke " +
//        "FROM workouts AS w " +
//        "INNER JOIN `workout-sets` as s " +
//        "ON w.id = s.workoutID")
data class WorkoutWithUoM(val uoM: WorkoutUoM,
                          val workoutDate: Date,
                          val reps: Int,
                          val distance: Int,
                          val stroke: String)
