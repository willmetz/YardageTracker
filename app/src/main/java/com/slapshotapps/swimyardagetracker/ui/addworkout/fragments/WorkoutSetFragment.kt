package com.slapshotapps.swimyardagetracker.ui.addworkout.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.slapshotapps.swimyardagetracker.R
import com.slapshotapps.swimyardagetracker.databinding.FragmentWorkoutSetBinding
import com.slapshotapps.swimyardagetracker.ui.addworkout.viewmodels.WorkoutSetViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [WorkoutSetFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class WorkoutSetFragment : Fragment(), WorkoutSetViewModel.WorkoutSetViewModelInterface {

    private lateinit var binding: FragmentWorkoutSetBinding
    private lateinit var viewModel: WorkoutSetViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_workout_set, container, false)
        viewModel = WorkoutSetViewModel()

        binding.item = viewModel

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
