package com.slapshotapps.swimyardagetracker.ui.addworkout.fragments


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.slapshotapps.swimyardagetracker.R
import com.slapshotapps.swimyardagetracker.databinding.FragmentWorkoutSummaryBinding
import com.slapshotapps.swimyardagetracker.ui.addworkout.adapters.WorkoutSummaryAdapter
import com.slapshotapps.swimyardagetracker.ui.addworkout.viewmodels.WorkoutSummaryItemViewModel
import com.slapshotapps.swimyardagetracker.ui.addworkout.viewmodels.WorkoutSummaryViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [WorkoutSummaryFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class WorkoutSummaryFragment : Fragment(), WorkoutSummaryViewModel.WorkoutSummaryListener {

    lateinit var binding: FragmentWorkoutSummaryBinding
    lateinit var viewModel: WorkoutSummaryViewModel

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        viewModel = ViewModelProviders.of(this).get(WorkoutSummaryViewModel::class.java)
        viewModel.listener = this
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_workout_summary, container, false)

        binding.item = viewModel

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        hideKeyboard()

        viewModel.onViewReady()
    }

    override fun onWorkoutDataReady(workoutSummaryItemViewModels: List<WorkoutSummaryItemViewModel>) {
        val adapter = WorkoutSummaryAdapter(workoutSummaryItemViewModels)

        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.workoutSets.layoutManager = layoutManager
        binding.workoutSets.addItemDecoration(DividerItemDecoration(context, layoutManager.getOrientation()))
        binding.workoutSets.adapter = adapter
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(binding.root.getWindowToken(), 0)
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
