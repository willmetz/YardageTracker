package com.slapshotapps.swimyardagetracker.ui.records.editRecord

import java.util.*

data class EditableRecordModel(
    val event: String,
    val yardageRecord: EditableTimeRecordModel? = null,
    val metricRecord: EditableTimeRecordModel? = null
)

data class EditableTimeRecordModel(
    val enabled: Boolean,
    val minutes: String,
    val seconds: String,
    val milliseconds: String,
    val recordDate: Date,
    val formattedDate: String
)
