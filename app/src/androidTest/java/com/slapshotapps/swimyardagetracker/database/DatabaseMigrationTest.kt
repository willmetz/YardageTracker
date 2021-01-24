package com.slapshotapps.swimyardagetracker.database

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.slapshotapps.swimyardagetracker.database.extensions.blockingObserve
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutUoM
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*


@RunWith(AndroidJUnit4::class)
class DatabaseMigrationTest {

    private val TEST_DB = "migration-test"

    @Rule @JvmField public val rule = InstantTaskExecutorRule()

    @get:Rule
    public val helper: MigrationTestHelper = MigrationTestHelper(
            InstrumentationRegistry.getInstrumentation(),
            WorkoutDatabase::class.java.canonicalName,
            FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    fun migrate1To2() {

        val recordDate = Date()

        var database = helper.createDatabase(TEST_DB, 1).apply {
            // db has schema version 1. insert some data using SQL queries.
            // You cannot use DAO classes because they expect the latest schema.
            val contentValues = ContentValues()
            contentValues.put("id", 1)
            contentValues.put("uoM", WorkoutUoM.YARDS.label)
            contentValues.put("workoutDate", recordDate.time)
            contentValues.put("updateDate", recordDate.time)

            insert("workouts", SQLiteDatabase.CONFLICT_REPLACE, contentValues);
            // Prepare for the next version.
            close()
        }


        helper.runMigrationsAndValidate(TEST_DB, 2, false, MIGRATION_1_2)

        // Re-open the database with version 2 and provide
        // MIGRATION_1_2 as the migration process.
        val migratedDB = Room.databaseBuilder(
                InstrumentationRegistry.getInstrumentation().getTargetContext(),
                WorkoutDatabase::class.java,
                TEST_DB)
                .addMigrations(MIGRATION_1_2)
                .build()

        val workouts = migratedDB.workoutDao().getWorkouts().blockingObserve()

        assert(workouts != null)

        assert(workouts?.count() == 1)
        assert(workouts?.get(0)?.id == 1L)
        assert(workouts?.get(0)?.uoM == WorkoutUoM.YARDS)
    }


}