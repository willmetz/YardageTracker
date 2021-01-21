package com.slapshotapps.swimyardagetracker.database

import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutUoM
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*
import kotlin.jvm.Throws


@RunWith(AndroidJUnit4::class)
class DatabaseMigrationTest {

    private val TEST_DB = "migration-test"

    @Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
            InstrumentationRegistry.getInstrumentation(),
            WorkoutDatabase::class.java.canonicalName,
            FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    @Throws(IOException::class)
    fun migrate1To2() {

        val recordDate = Date()

        var database = helper.createDatabase(TEST_DB, 1).apply {
            // db has schema version 1. insert some data using SQL queries.
            // You cannot use DAO classes because they expect the latest schema.
            execSQL("INSERT INTO workouts (id, uoM, workoutDate, updateDate) VALUES(1,`yards`,${recordDate.time}, ${recordDate.time})")

            // Prepare for the next version.
            close()
        }

        // Re-open the database with version 2 and provide
        // MIGRATION_1_2 as the migration process.
        val migratedDB = Room.databaseBuilder(ApplicationProvider.getApplicationContext(),
                WorkoutDatabase::class.java, TEST_DB)
                .addMigrations(MIGRATION_1_2)
                .build()

        migratedDB.workoutDao().getWorkouts().observeForever{}

        val workouts = migratedDB.workoutDao().getWorkouts().value

        assert(workouts != null)

        assert(workouts?.count() == 1)
        assert(workouts?.get(0)?.id == 1L)
        assert(workouts?.get(0)?.uoM == WorkoutUoM.YARDS)
    }
}