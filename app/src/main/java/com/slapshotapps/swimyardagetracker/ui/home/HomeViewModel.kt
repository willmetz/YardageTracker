package com.slapshotapps.swimyardagetracker.ui.home

import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    fun lastWorkoutYardage() : String{
        return "1200 yards"
    }

    fun lastWorkoutDate(): String{
        return "Friday July 6th 2018"
    }

    fun totalWorkoutsForYear() : String{
        return "8"
    }

    fun totalYardageForYear(): String{
        return "5000"
    }
}
