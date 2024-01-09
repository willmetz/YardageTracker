package com.slapshotapps.swimyardagetracker.ui.addworkout.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.slapshotapps.swimyardagetracker.R
import com.slapshotapps.swimyardagetracker.databinding.FragmentWorkoutDateBinding
import com.slapshotapps.swimyardagetracker.ui.addworkout.viewmodels.WorkoutDateViewModel
import dagger.android.support.AndroidSupportInjection
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [WorkoutDateFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class WorkoutDateFragment : Fragment(R.layout.fragment_workout_date), DatePickerDialog.OnDateSetListener {

    private var datePickerDialog: DatePickerDialog? = null
    private var binding: FragmentWorkoutDateBinding? = null

    @Inject
    lateinit var viewModel: WorkoutDateViewModel

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this) // Providing the dependency, must call before super
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWorkoutDateBinding.bind(view)

        binding?.workoutDate?.setOnClickListener { onShowDateSelection(viewModel.getWorkoutDate()) }
        binding?.workoutDateLabel?.setOnClickListener { onShowDateSelection(viewModel.getWorkoutDate()) }
        binding?.addWorkoutButton?.setOnClickListener { onSetWorkoutUoM() }
        binding?.workoutDate?.text = viewModel.getFormattedWorkoutDate()
    }

    private fun onShowDateSelection(currentWorkoutDate: Date) {
        val calendar = Calendar.getInstance()
        calendar.time = currentWorkoutDate
        datePickerDialog = DatePickerDialog(requireContext(), this, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        datePickerDialog?.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        viewModel.onDateChanged(calendar.time)
        binding?.workoutDate?.text = viewModel.getFormattedWorkoutDate()
    }

    private fun onSetWorkoutUoM() {
        NavHostFragment.findNavController(this).navigate(R.id.action_workoutDateFragment_to_workoutUnitOfMeasure)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using.
         *
         * @return A new instance of fragment WorkoutDateFragment.
         */
        @JvmStatic
        fun newInstance() =
                WorkoutDateFragment()
    }
}
