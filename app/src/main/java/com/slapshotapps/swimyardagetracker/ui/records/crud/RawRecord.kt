package com.slapshotapps.swimyardagetracker.ui.records.crud

import com.slapshotapps.swimyardagetracker.models.workout.WorkoutUoM
import java.util.*
import kotlin.time.Duration

data class RawRecord constructor(val stroke: String?,
                                   val distance: Int?,
                                   val dateOfRecord: Date,
                                   val unitOfMeasure: WorkoutUoM?,
                                   val time: TimeForPersonalRecord)