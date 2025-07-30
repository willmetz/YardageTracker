package com.slapshotapps.swimyardagetracker.ui.records.newRecord

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.slapshotapps.swimyardagetracker.R
import com.slapshotapps.swimyardagetracker.SwimYardageTrackerApp
import com.slapshotapps.swimyardagetracker.databinding.FragmentAddPersonalRecordBinding
import com.slapshotapps.swimyardagetracker.extensions.SpinnerItemSelectedListener
import com.slapshotapps.swimyardagetracker.extensions.setSpinnerEntries
import com.slapshotapps.swimyardagetracker.extensions.setSpinnerItemSelectedListener
import com.slapshotapps.swimyardagetracker.utils.TextWatcherHelper
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import java.util.*
import javax.inject.Inject
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [AddNewPersonalRecordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddNewPersonalRecordFragment : DaggerFragment(), OnDateSetListener {

    private var _binding: FragmentAddPersonalRecordBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModel: AddNewPersonalRecordViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPersonalRecordBinding.inflate(inflater, container, false)

        viewModel.viewModelEvent.observe(viewLifecycleOwner, androidx.lifecycle.Observer { onViewModelEvent(it) })

        binding.dateEntry.setOnClickListener {
            showCalendar()
        }

        binding.dateIcon.setOnClickListener {
            showCalendar()
        }

        binding.save.setOnClickListener {
            onSaveRecord()
        }

        binding.cancel.setOnClickListener {
            NavHostFragment.findNavController(this).popBackStack()
        }

        setupTextWatchers()

        setupUnitOfMeasureList()

        return binding.root
    }

    private fun showCalendar() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(requireContext(), this, calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.show()
    }

    private fun onSaveRecord() {
        val distance = binding.distanceInput.text.toString().toIntOrNull() ?: 0
        val stroke = binding.strokeInput.text.toString()

        val timeForRecord = binding.timeEntry.getTimeEntered()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.onAddNewRecord(stroke, distance, timeForRecord)
        }
    }

    private fun setupTextWatchers() {
        binding.distance.editText?.addTextChangedListener(object : TextWatcherHelper() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // clear any errors
                binding.distance.error = null
            }
        })

        binding.stroke.editText?.addTextChangedListener(object : TextWatcherHelper() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // clear any errors
                binding.stroke.error = null
            }
        })
    }

    private fun setupUnitOfMeasureList() {
        val unitsOfMeasure = resources.getStringArray(R.array.unit_of_measure_list)
        binding.uom.setSpinnerEntries(listOf(*unitsOfMeasure))

        binding.uom.setSpinnerItemSelectedListener(object : SpinnerItemSelectedListener {
            override fun onItemSelected(item: Any) {
                if (item is String) {
                    viewModel.updateUnitOfMeasure(item)
                }
            }
        })
    }

    private fun onViewModelEvent(event: AddNewPersonalRecordEvent) {
        when (event) {
            is AddNewPersonalRecordEvent.OnDateChanged -> {
                binding.dateEntry.text = event.formattedDate
            }
            is AddNewPersonalRecordEvent.OnNewRecordAddedSuccess -> {
                Toast.makeText(context, event.msg, Toast.LENGTH_SHORT).show()
                NavHostFragment.findNavController(this).popBackStack()
            }
            is AddNewPersonalRecordEvent.OnFailureToAddRecord -> Toast.makeText(context, event.msg, Toast.LENGTH_SHORT).show()
            is AddNewPersonalRecordEvent.OnInvalidStroke -> binding.stroke.error = event.msg
            is AddNewPersonalRecordEvent.OnInvalidDistance -> binding.distance.error = event.msg
            is AddNewPersonalRecordEvent.OnInvalidRecord -> Toast.makeText(context, event.msg, Toast.LENGTH_SHORT).show()
            is AddNewPersonalRecordEvent.OnInvalidTimeEntry -> {
                Toast.makeText(context, event.msg, Toast.LENGTH_SHORT).show()
                binding.timeEntry.setError()
            }
            is AddNewPersonalRecordEvent.OnReady -> {
                // hide spinner?
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        val dateOfRecord = Calendar.getInstance()
        dateOfRecord.set(year, month, dayOfMonth)

        viewModel.onDateChanged(dateOfRecord.time)
    }
}
