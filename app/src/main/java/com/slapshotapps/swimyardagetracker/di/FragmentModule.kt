package com.slapshotapps.swimyardagetracker.di

import com.slapshotapps.swimyardagetracker.ui.addworkout.fragments.WorkoutDateFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeWorkoutDateFragment(): WorkoutDateFragment
}