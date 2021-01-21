package com.slapshotapps.swimyardagetracker.models.personalrecords

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.slapshotapps.swimyardagetracker.models.workout.Workout
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutUoM
import java.util.*

@Keep
@Entity(tableName = "personal-records", indices = [Index("stroke"), Index("distance")])
data class PersonalRecord(@PrimaryKey(autoGenerate = true) var id: Long,
                          val stroke: String,
                          val distance: Int,
                          val time: List<RecordTime>)

@Keep
@Entity(tableName = "record-time", foreignKeys = arrayOf(ForeignKey(
        entity = Workout::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("recordID"),
        onDelete = ForeignKey.CASCADE)), indices = arrayOf(Index("recordID")))
data class RecordTime(@PrimaryKey(autoGenerate = true) val id: Long,
                      var recordID: Long,
                      val date: Date,
                      val unitOfMeasure: WorkoutUoM,
                      val hours: Int,
                      val minutes: Int,
                      val seconds: Int,
                      val milliseconds: Int)