package com.slapshotapps.swimyardagetracker.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.slapshotapps.swimyardagetracker.databinding.WorkoutSummaryLineItemBinding

class WorkoutHistoryAdapter(private val workoutSummaryItemViewModels: List<WorkoutSummaryItemViewModel>) :
    RecyclerView.Adapter<WorkoutHistoryAdapter.WorkoutHistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutHistoryViewHolder {
        val binding = WorkoutSummaryLineItemBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)

        return WorkoutHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutHistoryViewHolder, position: Int) {
        holder.onBind(workoutSummaryItemViewModels[position])
    }

    override fun getItemCount(): Int {
        return workoutSummaryItemViewModels.size
    }

    inner class WorkoutHistoryViewHolder(private val binding: WorkoutSummaryLineItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun onBind(item: WorkoutSummaryItemViewModel) {
                binding.workoutDate.text = item.workoutDate
                binding.workoutDistance.text = item.totalDistanceWithUoM
                binding.strokesUsed.text = item.workoutStrokes
            }
        }
}
