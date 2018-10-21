package com.slapshotapps.swimyardagetracker.ui.addworkout.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.slapshotapps.swimyardagetracker.extensions.SpinnerItemSelectedListener
import com.slapshotapps.swimyardagetracker.managers.WorkoutManager
import com.slapshotapps.swimyardagetracker.models.workoutsets.WorkoutUoM
import javax.inject.Inject


class WorkoutUoMViewModel@Inject constructor(private val workoutManager: WorkoutManager)  :
        ViewModel(), SpinnerItemSelectedListener {

    var uomEntries: ObservableField<List<String>>
    var uomValue = "Yards"
    var listener: WorkoutViewModelUoMListener? = null

    init {
        uomEntries = ObservableField(arrayListOf("Yards", "Meters"))
    }

    interface WorkoutViewModelUoMListener {
        fun onAddWorkouts()
        fun onChangeDate()
    }

    fun onNextTapped() {
        if(uomValue.equals("yards", true)){
            workoutManager.unitOfMeasure = WorkoutUoM.YARDS
        }else{
            workoutManager.unitOfMeasure = WorkoutUoM.METERS
        }
        listener?.onAddWorkouts()
    }

    fun onPreviousTapped() {
        listener?.onChangeDate()
    }

    override fun onItemSelected(item: Any) {
        if (item is String) {
            uomValue = item
        }
    }
}