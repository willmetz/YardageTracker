package com.slapshotapps.swimyardagetracker.ui.records

import com.slapshotapps.swimyardagetracker.models.personalrecords.PersonalRecordWithTimes
import com.slapshotapps.swimyardagetracker.models.personalrecords.RecordTime
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutUoM
import java.text.SimpleDateFormat
import java.util.*

class PersonalRecordItemViewModel(private val eventsWithTimes: PersonalRecordWithTimes) {

    val dateFormatter: SimpleDateFormat = SimpleDateFormat("M/d/yyyy", Locale.US)

    val mostRecentRecordDate: Date
        get() {
            var recordDate = eventsWithTimes.times[0].date
            if (eventsWithTimes.times.size > 1) {
                eventsWithTimes.times.forEach {
                    if (it.date.after(recordDate)) {
                        recordDate = it.date
                    }
                }
            }

            return recordDate
        }

    var event: String = ""
    get() {
        return "${eventsWithTimes.record.distance} ${eventsWithTimes.record.stroke}"
    }
    private set

    var yardsTime: String = ""
    get() {
        return getTimeForRecord(WorkoutUoM.YARDS)
    }
    private set

    var yardsDate: String = ""
    get() {
        return getDateForRecord(WorkoutUoM.YARDS)
    }
    private set

    var metricTime: String = ""
        get() {
            return getTimeForRecord(WorkoutUoM.METERS)
        }
        private set

    var metricDate: String = ""
        get() {
            return getDateForRecord(WorkoutUoM.METERS)
        }
        private set

    private fun getDateForRecord(unitOfMeasure: WorkoutUoM): String {
        return when (unitOfMeasure) {
            WorkoutUoM.METERS -> {
                val record = eventsWithTimes.times.firstOrNull { t -> t.unitOfMeasure == WorkoutUoM.METERS }
                if (record != null) {
                    dateFormatter.format(record.date)
                } else {
                    ""
                }
            }
            WorkoutUoM.YARDS -> {
                val record = eventsWithTimes.times.firstOrNull { t -> t.unitOfMeasure == WorkoutUoM.YARDS }
                if (record == null) {
                    ""
                } else {
                    dateFormatter.format(record.date)
                }
            }
        }
    }

    private fun getTimeForRecord(uOfm: WorkoutUoM): String {
        return when (uOfm) {
            WorkoutUoM.METERS -> {
                val record = eventsWithTimes.times.firstOrNull { t -> t.unitOfMeasure == WorkoutUoM.METERS }
                if (record == null) {
                    ""
                } else {
                    "Meters: " + getHours(record) + getMinutes(record) + getSeconds(record) + getMilliseconds(record)
                }
            }
            WorkoutUoM.YARDS -> {
                val record = eventsWithTimes.times.firstOrNull { t -> t.unitOfMeasure == WorkoutUoM.YARDS }
                if (record == null) {
                    ""
                } else {
                    "Yards: " + getHours(record) + getMinutes(record) + getSeconds(record) + getMilliseconds(record)
                }
            }
        }
    }

    private fun getHours(time: RecordTime): String {
        return when {
            time.hours <= 0 -> ""
            else -> String.format("%02d:", time.hours)
        }
    }

    private fun getMinutes(time: RecordTime): String {
        return when {
            time.minutes <= 0 -> ""
            else -> String.format("%02d:", time.minutes)
        }
    }

    private fun getSeconds(time: RecordTime): String {
        return when {
            time.hours <= 0 && time.minutes > 0 -> "00:"
            time.hours <= 0 -> "00"
            else -> String.format("%02d:", time.seconds)
        }
    }

    private fun getMilliseconds(time: RecordTime): String {
        return when {
            time.hours <= 0 -> ""
            else -> String.format("%02d", time.milliseconds)
        }
    }
}
