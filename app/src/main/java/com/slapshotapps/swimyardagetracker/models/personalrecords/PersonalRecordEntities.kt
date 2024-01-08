package com.slapshotapps.swimyardagetracker.models.personalrecords

import androidx.annotation.Keep
import androidx.room.*
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutUoM
import java.util.*

@Keep
@Entity(tableName = "personal-records", indices = [Index("stroke"), Index("distance")])
data class PersonalRecord(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    val stroke: String,
    val distance: Int
)

@Keep
@Entity(tableName = "record-time", foreignKeys = arrayOf(ForeignKey(
        entity = PersonalRecord::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("recordID"),
        onDelete = ForeignKey.CASCADE)), indices = arrayOf(Index("recordID")))
data class RecordTime(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    var recordID: Long,
    val date: Date,
    val unitOfMeasure: WorkoutUoM,
    val hours: Int,
    val minutes: Int,
    val seconds: Int,
    val milliseconds: Int
)

@Keep
data class PersonalRecordWithTimes(
    @Embedded val record: PersonalRecord,
    @Relation(parentColumn = "id", entityColumn = "recordID")val times: List<RecordTime>
)
