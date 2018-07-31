package com.slapshotapps.swimyardagetracker.ui.home

import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    interface HomeViewModelListener{
        fun onAddWorkout()
    }

    private var listener : HomeViewModelListener ?= null

    fun setListener(listener: HomeViewModelListener){
        this.listener = listener
    }

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

    fun onAddWorkoutTapped(){
        listener?.onAddWorkout()
    }
}
