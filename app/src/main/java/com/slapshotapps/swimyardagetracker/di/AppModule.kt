package com.slapshotapps.swimyardagetracker.di

import android.app.Application
import androidx.room.Room
import com.slapshotapps.swimyardagetracker.database.MIGRATION_1_2
import com.slapshotapps.swimyardagetracker.database.WorkoutDatabase
import com.slapshotapps.swimyardagetracker.managers.WorkoutManager
import com.slapshotapps.swimyardagetracker.utils.StringProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class AppModule {

    @Provides
    @Singleton
    fun providesWorkoutMananger(): WorkoutManager {
        return WorkoutManager()
    }

    @Provides
    @Singleton
    fun providesDatabase(application: Application): WorkoutDatabase {
        return Room.databaseBuilder(application.applicationContext, WorkoutDatabase::class.java, "WorkoutDB")
                .addMigrations(MIGRATION_1_2)
                .build()
    }

    @Provides
    @Singleton
    fun providesStrings(application: Application): StringProvider = StringProvider(application)

    @Provides
    @Singleton
    fun providesIOScope(): CoroutineDispatcher = Dispatchers.IO
}
