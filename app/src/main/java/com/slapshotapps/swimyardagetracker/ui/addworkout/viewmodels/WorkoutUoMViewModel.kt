package com.slapshotapps.swimyardagetracker.ui.addworkout.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.slapshotapps.swimyardagetracker.extensions.SpinnerItemSelectedListener


class WorkoutUoMViewModel() : ViewModel(), SpinnerItemSelectedListener {

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