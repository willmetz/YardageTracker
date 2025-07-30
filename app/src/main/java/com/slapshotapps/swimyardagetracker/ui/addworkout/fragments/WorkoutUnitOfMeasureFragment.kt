package com.slapshotapps.swimyardagetracker.ui.addworkout.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.slapshotapps.swimyardagetracker.R
import com.slapshotapps.swimyardagetracker.databinding.FragmentWorkoutUnitOfMeasureBinding
import com.slapshotapps.swimyardagetracker.extensions.SpinnerItemSelectedListener
import com.slapshotapps.swimyardagetracker.extensions.setSpinnerEntries
import com.slapshotapps.swimyardagetracker.extensions.setSpinnerItemSelectedListener
import com.slapshotapps.swimyardagetracker.ui.addworkout.viewmodels.WorkoutUoMViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [WorkoutUnitOfMeasureFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class WorkoutUnitOfMeasureFragment : DaggerFragment(R.layout.fragment_workout_unit_of_measure), SpinnerItemSelectedListener {

    private var binding: FragmentWorkoutUnitOfMeasureBinding? = null

    @Inject
    lateinit var viewModel: WorkoutUoMViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentWorkoutUnitOfMeasureBinding.bind(view)
        binding?.uom?.setSpinnerEntries(viewModel.uomEntries)
        binding?.uom?.setSpinnerItemSelectedListener(this)

        binding?.previous?.setOnClickListener { onChangeDate() }
        binding?.next?.setOnClickListener { onAddWorkouts() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun onAddWorkouts() {
        viewModel.onNext()
        NavHostFragment.findNavController(this).navigate(R.id.action_workoutUnitOfMeasure_to_workoutSetFragment)
    }

    private fun onChangeDate() {
        NavHostFragment.findNavController(this).popBackStack()
    }

    override fun onItemSelected(item: Any) {
        if (item is String) {
            viewModel.uomValue = item
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment
         *
         * @return A new instance of fragment WorkoutUnitOfMeasureFragment.
         */
        @JvmStatic
        fun newInstance() =
                WorkoutUnitOfMeasureFragment()
    }
}
