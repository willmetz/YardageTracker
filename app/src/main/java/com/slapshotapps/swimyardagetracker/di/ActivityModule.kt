package com.slapshotapps.swimyardagetracker.di

import com.slapshotapps.swimyardagetracker.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(
        modules = [
            FragmentModule::class
        ]
    )
    abstract fun contributeMainActivity(): MainActivity
}
