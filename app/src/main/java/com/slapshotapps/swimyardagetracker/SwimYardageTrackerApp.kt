package com.slapshotapps.swimyardagetracker

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import com.slapshotapps.swimyardagetracker.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class SwimYardageTrackerApp : Application(), HasActivityInjector, HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var mAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder() // Building the app component
                .app(this)
                .build()
                .inject(this) // Injecting our android injector
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity> = mAndroidInjector

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector
}
