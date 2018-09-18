package com.slapshotapps.swimyardagetracker.ui.addworkout.viewmodels

import com.slapshotapps.swimyardagetracker.extensions.SpinnerItemSelectedListener


class WorkoutUoMViewModel(private val listener: WorkoutViewModelUoMListener): SpinnerItemSelectedListener {

    interface WorkoutViewModelUoMListener{
        fun onAddWorkouts()
        fun onChangeDate()
    }

    fun onNextTapped(){
        listener.onAddWorkouts()
    }

    fun onPreviousTapped(){
        listener.onChangeDate()
    }

    fun getUomValues(): List<String>{
        return listOf("yards", "meters")
    }

    override fun onItemSelected(item: Any) {
        if(item is String){
            //todo
        }
    }
}