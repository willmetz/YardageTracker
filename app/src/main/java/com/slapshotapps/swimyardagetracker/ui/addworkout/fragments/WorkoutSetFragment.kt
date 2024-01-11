package com.slapshotapps.swimyardagetracker.ui.addworkout.fragments

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.slapshotapps.swimyardagetracker.R
import com.slapshotapps.swimyardagetracker.databinding.FragmentWorkoutSetBinding
import com.slapshotapps.swimyardagetracker.ui.addworkout.viewmodels.WorkoutSetViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [WorkoutSetFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class WorkoutSetFragment : Fragment(R.layout.fragment_workout_set), WorkoutSetViewModel.WorkoutSetViewModelListener {

    private var binding: FragmentWorkoutSetBinding? = null

    @Inject
    lateinit var viewModel: WorkoutSetViewModel

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this) // Providing the dependency, must call before super
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentWorkoutSetBinding.bind(view)
        val hintedStrokes = arrayOf("Fly", "Free", "Back", "Breast", "IM", "Kick")
        val autoCompleteAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, hintedStrokes)
        binding?.stokeAutocompleteView?.setAdapter(autoCompleteAdapter)

        binding?.next?.setOnClickListener {
            val reps = binding?.repsInput?.text.toString()
            val distance = binding?.distanceInput?.text.toString()
            val stroke = binding?.stokeAutocompleteView?.text.toString()
            viewModel.onNextTapped(reps, distance, stroke)
        }

        binding?.addAnother?.setOnClickListener {
            val reps = binding?.repsInput?.text.toString()
            val distance = binding?.distanceInput?.text.toString()
            val stroke = binding?.stokeAutocompleteView?.text.toString()
            viewModel.addAnotherSet(reps, distance, stroke)
        }

        binding?.stokeAutocompleteView?.setOnEditorActionListener { textView, action, keyEvent ->
            if (keyEvent?.keyCode == KeyEvent.KEYCODE_ENTER || action == EditorInfo.IME_ACTION_DONE) {
                val reps = binding?.repsInput?.text.toString()
                val distance = binding?.distanceInput?.text.toString()
                val stroke = binding?.stokeAutocompleteView?.text.toString()
                viewModel.addAnotherSet(reps, distance, stroke)
                true
            } else false
        }
    }

    override fun onResume() {
        super.onResume()
        binding?.reps?.requestFocus()
        viewModel.listener = this
    }

    override fun onPause() {
        super.onPause()
        viewModel.listener = null
    }

    override fun onRepsEntryError(resID: Int) {
        binding?.reps?.error = getString(resID)
    }

    override fun onDistanceEntryError(resID: Int) {
        binding?.distance?.error = getString(resID)
    }

    override fun onStrokeEntryError(resID: Int) {
        binding?.stroke?.error = getString(resID)
    }

    override fun onShowNoSetErrorMsg(resID: Int) {
        Toast.makeText(requireContext(), resID, Toast.LENGTH_SHORT).show()
    }

    override fun onValidSetAdded() {
        binding?.let {
            it.reps.error = null
            it.distance.error = null
            it.stroke.error = null
            it.repsInput.text = null
            it.distanceInput.text = null
            it.stokeAutocompleteView.text = null
            it.reps.requestFocus()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onShowWorkoutSummary() {
        NavHostFragment.findNavController(this).navigate(R.id.action_workoutSetFragment_to_workoutSummaryFragment2)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment.
         *
         * @return A new instance of fragment WorkoutSetFragment.
         */
        @JvmStatic
        fun newInstance() =
                WorkoutSetFragment()
    }
}
