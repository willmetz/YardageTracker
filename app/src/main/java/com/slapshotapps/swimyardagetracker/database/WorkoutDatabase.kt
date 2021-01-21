package com.slapshotapps.swimyardagetracker.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.slapshotapps.swimyardagetracker.models.personalrecords.PersonalRecord
import com.slapshotapps.swimyardagetracker.models.personalrecords.RecordTime
import com.slapshotapps.swimyardagetracker.models.workout.Workout
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutSet


@Database(entities = arrayOf(Workout::class, WorkoutSet::class, PersonalRecord::class, RecordTime::class), version = 2, exportSchema = true)
@TypeConverters(DatabaseTypeConverters::class)
abstract class WorkoutDatabase: RoomDatabase() {

    abstract fun workoutDao(): WorkoutDAO
}


val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE IF NOT EXISTS `personal-records` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `stroke` TEXT NOT NULL, `distance` INTEGER NOT NULL, `time` TEXT NOT NULL)")
        database.execSQL("CREATE TABLE IF NOT EXISTS `record-time` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `recordID` INTEGER NOT NULL, `date` INTEGER NOT NULL, `unitOfMeasure` TEXT NOT NULL, `hours` INTEGER NOT NULL, `minutes` INTEGER NOT NULL, `seconds` INTEGER NOT NULL, `milliseconds` INTEGER NOT NULL, FOREIGN KEY(`recordID`) REFERENCES `workouts`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")

        database.execSQL("CREATE INDEX IF NOT EXISTS `index_personal-records_stroke` ON `personal-records` (`stroke`)")
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_personal-records_distance` ON `personal-records` (`distance`)")
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_record-time_recordID` ON `record-time` (`recordID`)")
    }
}