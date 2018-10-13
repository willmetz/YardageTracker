package com.slapshotapps.swimyardagetracker.ui.addworkout.viewmodels

import androidx.databinding.ObservableField
import com.slapshotapps.swimyardagetracker.extensions.SpinnerItemSelectedListener


class WorkoutUoMViewModel(private val listener: WorkoutViewModelUoMListener) : SpinnerItemSelectedListener {

    var uomEntries: ObservableField<List<String>>
    var uomValue = "Yards"

    init {
        uomEntries = ObservableField(arrayListOf("Yards", "Meters"))
    }

    interface WorkoutViewModelUoMListener{
        fun onAddWorkouts()
        fun onChangeDate()
    }

    fun onNextTapped(){
        listener?.onAddWorkouts()
    }

    fun onPreviousTapped(){
        listener?.onChangeDate()
    }

    override fun onItemSelected(item: Any) {
        if(item is String){
            uomValue = item
        }
    }
}