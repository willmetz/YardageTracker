package com.slapshotapps.swimyardagetracker.ui.addworkout.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.slapshotapps.swimyardagetracker.R
import com.slapshotapps.swimyardagetracker.databinding.FragmentWorkoutSummaryBinding
import com.slapshotapps.swimyardagetracker.ui.addworkout.adapters.WorkoutSummaryAdapter
import com.slapshotapps.swimyardagetracker.ui.addworkout.viewmodels.WorkoutSummaryItemViewModel
import com.slapshotapps.swimyardagetracker.ui.addworkout.viewmodels.WorkoutSummaryViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [WorkoutSummaryFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class WorkoutSummaryFragment : Fragment(R.layout.fragment_workout_summary), WorkoutSummaryViewModel.WorkoutSummaryListener {

    private var binding: FragmentWorkoutSummaryBinding? = null

    @Inject
    lateinit var viewModel: WorkoutSummaryViewModel

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this) // Providing the dependency, must call before super
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWorkoutSummaryBinding.bind(view)

        binding?.workoutDate?.text = viewModel.getWorkoutDate()

        binding?.submit?.setOnClickListener { viewModel.onSubmitTapped() }
        binding?.cancel?.setOnClickListener { viewModel.onCancelTapped() }
    }

    override fun onResume() {
        super.onResume()

        hideKeyboard()

        viewModel.listener = this

        viewModel.onViewReady()
    }

    override fun onWorkoutDataReady(workoutSummaryItemViewModels: List<WorkoutSummaryItemViewModel>) {
        val adapter = WorkoutSummaryAdapter(workoutSummaryItemViewModels)

        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding?.workoutSets?.layoutManager = layoutManager
        binding?.workoutSets?.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))
        binding?.workoutSets?.adapter = adapter
    }

    override fun onWorkAdded() {
        Toast.makeText(context, "Successfully added workout!!", Toast.LENGTH_SHORT).show()

        NavHostFragment.findNavController(this).navigate(R.id.action_workoutSummaryFragment_to_homeFragment)
    }

    override fun onCancelAdd() {
        NavHostFragment.findNavController(this).navigate(R.id.action_workoutSummaryFragment_to_homeFragment)
    }

    override fun onErrorAddingWorkout(msgID: Int) {
        Toast.makeText(context, msgID, Toast.LENGTH_SHORT).show()
    }

    private fun hideKeyboard() {
        binding?.let {
            val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.hideSoftInputFromWindow(it.root.getWindowToken(), 0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment WorkoutSummaryFragment.
         */
        @JvmStatic
        fun newInstance() = WorkoutSummaryFragment()
    }
}
