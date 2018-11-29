package com.slapshotapps.swimyardagetracker.database

import androidx.room.TypeConverter
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutUoM
import java.util.*


class DatabaseTypeConverters {

    companion object {

        @TypeConverter
        @JvmStatic
        fun fromDate(date: Date?): Long {
            return date?.time ?: 0
        }

        @TypeConverter
        @JvmStatic
        fun toDate(timeSinceEpoch: Long?): Date? {
            return if (timeSinceEpoch == null) null else Date(timeSinceEpoch)
        }

        @TypeConverter
        @JvmStatic
        fun toWorkoutUofM(uom: String?): WorkoutUoM {
            if (uom?.isEmpty() == true) {
                return WorkoutUoM.YARDS
            }

            return WorkoutUoM.valueOf(uom!!)
        }

        @TypeConverter
        @JvmStatic
        fun fromWorkoutUofM(workoutUoM: WorkoutUoM): String {
            return workoutUoM.toString()
        }

    }

}