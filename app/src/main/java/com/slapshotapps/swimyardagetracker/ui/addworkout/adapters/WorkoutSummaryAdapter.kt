package com.slapshotapps.swimyardagetracker.ui.addworkout.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.slapshotapps.swimyardagetracker.databinding.WorkoutLineItemBinding
import com.slapshotapps.swimyardagetracker.ui.addworkout.viewmodels.WorkoutSummaryItemViewModel

class WorkoutSummaryAdapter(private val workoutSummaryItemViewModels: List<WorkoutSummaryItemViewModel>) :
        RecyclerView.Adapter<WorkoutSummaryAdapter.WorkoutSummaryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutSummaryViewHolder {
        val binding = WorkoutLineItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return WorkoutSummaryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutSummaryViewHolder, position: Int) {
        holder.onBind(workoutSummaryItemViewModels[position])
    }

    override fun getItemCount(): Int {
        return workoutSummaryItemViewModels.size
    }

    inner class WorkoutSummaryViewHolder(private val binding: WorkoutLineItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun onBind(model: WorkoutSummaryItemViewModel) {
                binding.reps.text = model.getReps()
                binding.distance.text = model.getDistance()
                binding.stroke.text = model.stroke
                binding.uom.text = model.uom
            }
    }
}
