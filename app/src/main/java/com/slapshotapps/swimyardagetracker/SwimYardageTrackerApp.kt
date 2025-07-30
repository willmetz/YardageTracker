package com.slapshotapps.swimyardagetracker

import com.slapshotapps.swimyardagetracker.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class SwimYardageTrackerApp : DaggerApplication() {

    private val applicationInjector = DaggerAppComponent.builder().app(this).build()

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = applicationInjector

}
