package com.slapshotapps.swimyardagetracker.ui.records.editRecord

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.slapshotapps.swimyardagetracker.databinding.FragmentEditPersonalRecordBinding
import com.slapshotapps.swimyardagetracker.models.personalrecords.YardageTrackerRecordTime
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutUoM
import dagger.android.support.AndroidSupportInjection
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class EditPersonalRecordFragment : Fragment() {

    private var _binding: FragmentEditPersonalRecordBinding? = null
    private val binding get() = _binding!!

    private val args: EditPersonalRecordFragmentArgs by navArgs()

    @Inject
    lateinit var viewModel: EditPersonalRecordViewModel

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this) // Providing the dependency, must call before super
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditPersonalRecordBinding.inflate(inflater, container, false)

        viewModel.viewModelEvent.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer { event -> onViewModelEvent(event) })

        viewModel.getRecord(args.recordToEdit).observe(viewLifecycleOwner,
            androidx.lifecycle.Observer { event -> updateDisplayData(event) })

        binding.cancel.setOnClickListener { backOut() }

        binding.save.setOnClickListener { updateRecord() }

        initializeDateFields()

        return binding.root
    }

    private fun backOut() {
        NavHostFragment.findNavController(this).popBackStack()
    }

    private fun initializeDateFields() {
        binding.yardageDateEntry.setOnClickListener { showCalendar(WorkoutUoM.YARDS) }
        binding.yardageDateIcon.setOnClickListener { showCalendar(WorkoutUoM.YARDS) }

        binding.metricDateEntry.setOnClickListener { showCalendar(WorkoutUoM.METERS) }
        binding.metricDateIcon.setOnClickListener { showCalendar(WorkoutUoM.METERS) }
    }

    private fun onViewModelEvent(event: EditPersonalRecordEvent) {
        when (event) {
            is EditPersonalRecordEvent.Ready -> {
                // hide spinner?
            }
            is EditPersonalRecordEvent.Loading -> {
                // nothing at this time, perhaps a spinner?
            }
            is EditPersonalRecordEvent.DeleteRecordError -> {
                Toast.makeText(requireContext(), event.msg, Toast.LENGTH_SHORT).show()
            }
            is EditPersonalRecordEvent.Success -> {
                Toast.makeText(requireContext(), event.msg, Toast.LENGTH_SHORT).show()
                backOut()
            }
            is EditPersonalRecordEvent.InvalidTimeError -> {
                if (event.uom == WorkoutUoM.YARDS) {
                    binding.yardageTimeEntry.setError()
                } else {
                    binding.metricTimeEntry.setError()
                }
                Toast.makeText(requireContext(), event.msg, Toast.LENGTH_SHORT).show()
            }
            is EditPersonalRecordEvent.InvalidDate -> {
                if (event.uom == WorkoutUoM.YARDS) {
                    binding.yardageDateEntry.error = event.msg
                } else {
                    binding.metricDateEntry.error = event.msg
                }
            }
            is EditPersonalRecordEvent.UpdateRecordDisplayDate -> {
                if (event.uom == WorkoutUoM.YARDS) {
                    binding.yardageDateEntry.error = null
                    binding.yardageDateEntry.text = event.newDate
                } else
                    binding.metricDateEntry.error = null
                binding.metricDateEntry.text = event.newDate
            }
        }
    }

    private fun updateRecord() {

        val records = ArrayList<YardageTrackerRecordTime>()
        if (binding.yardageRecordEnabled.isChecked) {
            val dateOfRecord = viewModel.getDateFromString(binding.yardageDateEntry.text.toString())
            val timeEntered = binding.yardageTimeEntry.getTimeEntered()

            records.add(YardageTrackerRecordTime(date = dateOfRecord, uom = WorkoutUoM.YARDS,
                minutes = timeEntered.minutes, seconds = timeEntered.seconds, milliseconds = timeEntered.milliseconds))
        }

        if (binding.metricRecordEnabled.isChecked) {
            val dateOfRecord = viewModel.getDateFromString(binding.metricDateEntry.text.toString())
            val timeEntered = binding.metricTimeEntry.getTimeEntered()

            records.add(YardageTrackerRecordTime(date = dateOfRecord, uom = WorkoutUoM.METERS,
                minutes = timeEntered.minutes, seconds = timeEntered.seconds, milliseconds = timeEntered.milliseconds))
        }

        viewModel.updateRecord(records)
    }

    private fun showCalendar(workoutUoM: WorkoutUoM) {

        val date = Calendar.getInstance()
        val datePickerDialog: DatePickerDialog?

        if (workoutUoM == WorkoutUoM.YARDS) {
            val rawDate = viewModel.getDateFromString(binding.yardageDateEntry.text.toString())
            if (rawDate != null) date.time = rawDate
            datePickerDialog = DatePickerDialog(requireContext(),
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    viewModel.updateDate(year, month, dayOfMonth, WorkoutUoM.YARDS)
                },
                date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH))
        } else {
            val rawDate = viewModel.getDateFromString(binding.metricDateEntry.text.toString())
            if (rawDate != null) date.time = rawDate
            datePickerDialog = DatePickerDialog(requireContext(),
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    viewModel.updateDate(year, month, dayOfMonth, WorkoutUoM.METERS)
                },
                date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH))
        }

        datePickerDialog.show()
    }

    private fun updateDisplayData(displayData: EditableRecordModel) {
        binding.event.text = displayData.event
        if (displayData.yardageRecord != null) {
            val yardageData = displayData.yardageRecord
            binding.yardageRecordEnabled.isChecked = yardageData.enabled
            binding.yardageTimeEntry.setTime(yardageData.minutes, yardageData.seconds, yardageData.milliseconds)
            binding.yardageDateEntry.text = yardageData.formattedDate
        } else {
            binding.yardageRecordEnabled.isChecked = false
        }

        if (displayData.metricRecord != null) {
            val metricData = displayData.metricRecord
            binding.metricRecordEnabled.isChecked = metricData.enabled
            binding.metricTimeEntry.setTime(metricData.minutes, metricData.seconds, metricData.milliseconds)
            binding.metricDateEntry.text = metricData.formattedDate
        } else {
            binding.metricRecordEnabled.isChecked = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }
}
