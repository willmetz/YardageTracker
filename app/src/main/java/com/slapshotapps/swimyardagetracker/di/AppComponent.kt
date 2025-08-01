package com.slapshotapps.swimyardagetracker.di

import android.app.Application
import com.slapshotapps.swimyardagetracker.SwimYardageTrackerApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, ActivityModule::class, AppModule::class, FragmentModule::class])
interface AppComponent : AndroidInjector<SwimYardageTrackerApp>{
    override fun inject(application: SwimYardageTrackerApp)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun app(application: Application): Builder

        fun build(): AppComponent
    }
}
