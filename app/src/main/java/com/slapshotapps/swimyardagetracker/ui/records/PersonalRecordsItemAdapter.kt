package com.slapshotapps.swimyardagetracker.ui.records

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.slapshotapps.swimyardagetracker.databinding.PersonalRecordLineItemBinding

class PersonalRecordsItemAdapter(
    private val items: List<PersonalRecordItemViewModel>,
    private val onRecordSelected: (PersonalRecordItemViewModel) -> Unit,
    private val onDeleteRecord: (PersonalRecordItemViewModel) -> Unit
) :
        ListAdapter<PersonalRecordItemViewModel, PersonalRecordsItemAdapter.PersonalRecordViewHolder>(PersonalRecordItemDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonalRecordViewHolder {
        val binding = PersonalRecordLineItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PersonalRecordViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: PersonalRecordViewHolder, position: Int) {
        with(items[position]) {
            holder.binding.eventDescription.text = event
            holder.binding.metricDate.text = metricDate
            holder.binding.metricTime.text = metricTime
            holder.binding.yardageDate.text = yardsDate
            holder.binding.yardageTime.text = yardsTime

            holder.itemView.setOnClickListener {
                onRecordSelected(this)
            }

            holder.binding.deleteRecord.setOnClickListener {
                onDeleteRecord(this)
            }
        }
    }

    inner class PersonalRecordViewHolder(val binding: PersonalRecordLineItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    class PersonalRecordItemDiff : DiffUtil.ItemCallback<PersonalRecordItemViewModel>() {
        override fun areItemsTheSame(oldItem: PersonalRecordItemViewModel, newItem: PersonalRecordItemViewModel): Boolean {
            return oldItem.recordID == newItem.recordID
        }

        override fun areContentsTheSame(oldItem: PersonalRecordItemViewModel, newItem: PersonalRecordItemViewModel): Boolean {
            val eventsMatch = oldItem.event == newItem.event
            val yardMatch = oldItem.yardsDate == newItem.yardsDate && oldItem.yardsTime == newItem.yardsTime

            val metricMatch = oldItem.metricDate == newItem.metricDate && oldItem.metricTime == newItem.metricTime

            return eventsMatch && yardMatch && metricMatch
        }
    }
}
