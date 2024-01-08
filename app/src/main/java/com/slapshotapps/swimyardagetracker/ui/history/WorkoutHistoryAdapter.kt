package com.slapshotapps.swimyardagetracker.ui.history

import com.slapshotapps.swimyardagetracker.R
import com.slapshotapps.swimyardagetracker.widgets.DataBindingBaseAdapter

class WorkoutHistoryAdapter(private val workoutSummaryItemViewModels: List<WorkoutSummaryItemViewModel>) :
        DataBindingBaseAdapter() {

    override fun getObjForPosition(position: Int): Any {
        return workoutSummaryItemViewModels[position]
    }

    override fun getLayoutIdForPosition(position: Int): Int {
        return R.layout.workout_summary_line_item
    }

    override fun getItemCount(): Int {
        return workoutSummaryItemViewModels.size
    }
}
