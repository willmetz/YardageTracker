package com.slapshotapps.swimyardagetracker.models.personalrecords

import com.slapshotapps.swimyardagetracker.models.workout.WorkoutUoM
import java.util.*


data class RecordTime(val date: Date, val unitOfMeasure: WorkoutUoM, val hours: Int, val minutes: Int, val seconds: Int, val milliseconds: Int)

data class PersonalRecord( val stroke: String, val distance: Int, val time: List<RecordTime>)