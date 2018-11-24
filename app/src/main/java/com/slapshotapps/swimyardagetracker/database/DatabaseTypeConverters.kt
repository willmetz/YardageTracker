package com.slapshotapps.swimyardagetracker.database

import androidx.room.TypeConverter
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

    }

}