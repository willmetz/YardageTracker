package com.slapshotapps.swimyardagetracker.ui.records.crud

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
import com.slapshotapps.swimyardagetracker.databinding.FragmentPersonalRecordCrudBinding
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutUoM
import dagger.android.support.AndroidSupportInjection
import java.util.*
import javax.inject.Inject
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [PersonalRecordCrudFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PersonalRecordCrudFragment : Fragment(), OnDateSetListener {

    private var _binding: FragmentPersonalRecordCrudBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModel: PersonalRecordCrudViewModel

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this) // Providing the dependency, must call before super
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPersonalRecordCrudBinding.inflate(inflater, container, false)

        binding.dateEntry.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(context!!, this, calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.show()
        }

        viewModel.viewModelEvent.observe(viewLifecycleOwner, androidx.lifecycle.Observer { onViewModelEvent(it) })

        binding.save.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val recordMinutes = binding.minutes.editText?.text.toString().toIntOrNull() ?: 0
                val recordSeconds = binding.seconds.editText?.text.toString().toIntOrNull() ?: 0
                val recordMilliseconds = binding.milliseconds.editText?.text.toString().toIntOrNull() ?: 0
                val distance = binding.distance.editText.toString().toIntOrNull() ?: 0

                val timeForRecord = TimeForPersonalRecord(recordMinutes, recordSeconds, recordMilliseconds)
                val record = RawRecord(binding.stroke.editText.toString(),
                    distance, Date(), WorkoutUoM.YARDS, timeForRecord)
                viewModel.onAddNewRecord(record)
            }
        }
        return binding.root
    }

    fun onViewModelEvent(event: PersonalRecordCrudEvent) {
        when (event) {
            is PersonalRecordCrudEvent.OnDateChanged -> {
                binding.dateEntry.text = event.formattedDate
            }
            is PersonalRecordCrudEvent.OnNewRecordAddedSuccess -> TODO()
            is PersonalRecordCrudEvent.OnFailureToAddRecord -> TODO()
            is PersonalRecordCrudEvent.OnInvalidStroke -> Toast.makeText(context, event.msg, Toast.LENGTH_SHORT).show()
            is PersonalRecordCrudEvent.OnInvalidDistance -> binding.distance.error = event.msg
            is PersonalRecordCrudEvent.OnInvalidUnitOfMeasure -> Toast.makeText(context, event.msg, Toast.LENGTH_SHORT).show()
            is PersonalRecordCrudEvent.OnInvalidTime -> Toast.makeText(context, event.msg, Toast.LENGTH_SHORT).show()
            is PersonalRecordCrudEvent.OnRecordAlreadyExists -> Toast.makeText(context, event.msg, Toast.LENGTH_SHORT).show()
            PersonalRecordCrudEvent.OnLoading -> {
                // show spinner?
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
