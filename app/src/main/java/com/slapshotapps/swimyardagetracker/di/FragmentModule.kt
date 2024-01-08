package com.slapshotapps.swimyardagetracker.di

import com.slapshotapps.swimyardagetracker.ui.addworkout.fragments.WorkoutDateFragment
import com.slapshotapps.swimyardagetracker.ui.addworkout.fragments.WorkoutSetFragment
import com.slapshotapps.swimyardagetracker.ui.addworkout.fragments.WorkoutSummaryFragment
import com.slapshotapps.swimyardagetracker.ui.addworkout.fragments.WorkoutUnitOfMeasureFragment
import com.slapshotapps.swimyardagetracker.ui.history.HistoryFragment
import com.slapshotapps.swimyardagetracker.ui.home.HomeFragment
import com.slapshotapps.swimyardagetracker.ui.records.PersonalRecordsFragment
import com.slapshotapps.swimyardagetracker.ui.records.editRecord.EditPersonalRecordFragment
import com.slapshotapps.swimyardagetracker.ui.records.newRecord.AddNewPersonalRecordFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeWorkoutDateFragment(): WorkoutDateFragment

    @ContributesAndroidInjector
    abstract fun contributeUomFragment(): WorkoutUnitOfMeasureFragment

    @ContributesAndroidInjector
    abstract fun contributeWorkoutSetFragment(): WorkoutSetFragment

    @ContributesAndroidInjector
    abstract fun contributeWorkoutSummaryFragment(): WorkoutSummaryFragment

    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeHistoryFragment(): HistoryFragment

    @ContributesAndroidInjector
    abstract fun contributePersonalRecordsFragment(): PersonalRecordsFragment

    @ContributesAndroidInjector
    abstract fun contributeNewRecordFragment(): AddNewPersonalRecordFragment

    @ContributesAndroidInjector
    abstract fun contributeEditRecordFragment(): EditPersonalRecordFragment
}
