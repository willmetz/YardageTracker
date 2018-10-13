package com.slapshotapps.swimyardagetracker.ui.addworkout.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_workout_summary, container, false)


        viewModel = WorkoutSummaryViewModel(this)
        binding.item = viewModel

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        viewModel.onViewReady()
    }

    override fun onWorkoutDataReady(workoutSummaryItemViewModels: List<WorkoutSummaryItemViewModel>) {
        val adapter = WorkoutSummaryAdapter(workoutSummaryItemViewModels)

        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.workoutSets.layoutManager = layoutManager
        binding.workoutSets.addItemDecoration(DividerItemDecoration(context, layoutManager.getOrientation()))
        binding.workoutSets.adapter = adapter
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
