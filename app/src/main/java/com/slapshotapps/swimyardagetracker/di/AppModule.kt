package com.slapshotapps.swimyardagetracker.di

import android.app.Application
import androidx.room.Room
import com.slapshotapps.swimyardagetracker.database.WorkoutDatabase
import com.slapshotapps.swimyardagetracker.managers.WorkoutManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class AppModule {

    @Provides
    @Singleton
    fun providesWorkoutMananger(): WorkoutManager{
        return WorkoutManager()
    }

    @Provides
    @Singleton
    fun providesDatabase(application: Application): WorkoutDatabase{
        return Room.databaseBuilder(application.applicationContext, WorkoutDatabase::class.java, "WorkoutDB").build()
    }
}