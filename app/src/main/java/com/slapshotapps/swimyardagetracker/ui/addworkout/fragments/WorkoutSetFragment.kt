package com.slapshotapps.swimyardagetracker.ui.addworkout.fragments


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
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
class WorkoutSetFragment : Fragment(), WorkoutSetViewModel.WorkoutSetViewModelListener {

    private lateinit var binding: FragmentWorkoutSetBinding

    @Inject
    lateinit var viewModel: WorkoutSetViewModel

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this) // Providing the dependency, must call before super
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_workout_set, container, false)
        binding.item = viewModel


        val hintedStrokes = arrayOf("Fly", "Free", "Back", "Breast", "IM", "Kick")
        val autoCompleteAdapter = ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, hintedStrokes)

        binding.stokeAutocompleteView.setAdapter(autoCompleteAdapter)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.reps.requestFocus()

        viewModel.listener = this
    }

    override fun onPause() {
        super.onPause()

        viewModel.listener = null
    }

    override fun onRepsEntryError(resID: Int) {
        binding.reps.error = getString(resID)
    }

    override fun onDistanceEntryError(resID: Int) {
        binding.distance.error = getString(resID)
    }

    override fun onStrokeEntryError(resID: Int) {
        binding.stroke.error = getString(resID)
    }

    override fun onShowNoSetErrorMsg(resID: Int) {
        Toast.makeText(context!!, resID, Toast.LENGTH_SHORT).show()
    }

    override fun onValidSetAdded() {
        binding.reps.error = null
        binding.distance.error = null
        binding.stroke.error = null

        binding.reps.requestFocus()
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
