package com.slapshotapps.swimyardagetracker.di

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
}