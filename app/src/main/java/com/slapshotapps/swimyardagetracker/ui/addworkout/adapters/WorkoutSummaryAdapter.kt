package com.slapshotapps.swimyardagetracker.ui.addworkout.adapters

import com.slapshotapps.swimyardagetracker.R
import com.slapshotapps.swimyardagetracker.models.workoutsets.WorkoutSet
import com.slapshotapps.swimyardagetracker.ui.addworkout.viewmodels.WorkoutSummaryItemViewModel
import com.slapshotapps.swimyardagetracker.widgets.DataBindingBaseAdapter


class WorkoutSummaryAdapter(private val workoutSummaryItemViewModels:List<WorkoutSummaryItemViewModel>):
        DataBindingBaseAdapter() {

    override fun getObjForPosition(position: Int): Any {
        return workoutSummaryItemViewModels[position]
    }

    override fun getLayoutIdForPosition(position: Int): Int {
        return R.layout.workout_line_item
    }

    override fun getItemCount(): Int {
        return workoutSummaryItemViewModels.size
    }
}