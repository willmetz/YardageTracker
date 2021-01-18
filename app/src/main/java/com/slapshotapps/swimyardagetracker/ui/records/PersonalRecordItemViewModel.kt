package com.slapshotapps.swimyardagetracker.ui.records

import com.slapshotapps.swimyardagetracker.models.personalrecords.PersonalRecord
import com.slapshotapps.swimyardagetracker.models.personalrecords.RecordTime
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutUoM
import java.text.SimpleDateFormat
import java.util.*


class PersonalRecordItemViewModel(private val record: PersonalRecord) {

    val dateFormatter: SimpleDateFormat = SimpleDateFormat("M/d/yyyy", Locale.US)

    var event: String = ""
    get() {
        return "${record.distance} ${record.stroke}"
    }
    private set

    var yardsTime: String = ""
    get() {
        return getTimeForRecord(WorkoutUoM.YARDS)
    }
    private set

    var yardsDate: String = ""
    get(){
        return getDateForRecord(WorkoutUoM.YARDS)
    }
    private set

    var metricTime: String = ""
        get() {
            return getTimeForRecord(WorkoutUoM.METERS)
        }
        private set

    var metricDate: String = ""
        get(){
            return getDateForRecord(WorkoutUoM.METERS)
        }
        private set

    private fun getDateForRecord(unitOfMeasure: WorkoutUoM) : String{
        return when(unitOfMeasure){
            WorkoutUoM.METERS -> {
                val time = record.time.firstOrNull {
                    it.unitOfMeasure == WorkoutUoM.METERS
                };
                if(time == null) {
                    ""
                }else {
                    dateFormatter.format(time.date)
                }
            }
            WorkoutUoM.YARDS -> {
                val time = record.time.firstOrNull {
                    it.unitOfMeasure == WorkoutUoM.YARDS
                };
                if(time == null) {
                    ""
                }else {
                    dateFormatter.format(time.date)
                }
            }
        }
    }


    private fun getTimeForRecord(uOfm: WorkoutUoM) : String{
        return when(uOfm){
            WorkoutUoM.METERS -> {
                val time = record.time.firstOrNull {
                    it.unitOfMeasure == WorkoutUoM.METERS
                };
                if(time == null) {
                    ""
                }else {
                    getHours(time) + getMinutes(time) + getSeconds(time) + getMilliseconds(time)
                }
            }
            WorkoutUoM.YARDS -> {
                val time = record.time.firstOrNull {
                    it.unitOfMeasure == WorkoutUoM.YARDS
                };
                if(time == null) {
                    ""
                }else {
                    getHours(time) + getMinutes(time) + getSeconds(time) + getMilliseconds(time)
                }
            }
        }
    }

    private fun getHours(time: RecordTime) : String{
        return when{
            time.hours <= 0 -> ""
            else -> String.format("%02d:",time.hours)
        }
    }

    private fun getMinutes(time: RecordTime) : String{
        return when{
            time.minutes <= 0 -> ""
            else -> String.format("%02d:",time.minutes)
        }
    }

    private fun getSeconds(time: RecordTime) : String{
        return when{
            time.hours <= 0 && time.minutes > 0 -> "00:"
            time.hours <= 0 -> "00"
            else -> String.format("%02d:",time.seconds)
        }
    }

    private fun getMilliseconds(time: RecordTime) : String{
        return when{
            time.hours <= 0 -> ""
            else -> String.format("%02d",time.milliseconds)
        }
    }
}