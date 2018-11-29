package com.slapshotapps.swimyardagetracker.ui.addworkout.fragments


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
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
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [WorkoutSummaryFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class WorkoutSummaryFragment : Fragment(), WorkoutSummaryViewModel.WorkoutSummaryListener {

    lateinit var binding: FragmentWorkoutSummaryBinding

    @Inject
    lateinit var viewModel: WorkoutSummaryViewModel

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this) // Providing the dependency, must call before super
        super.onAttach(context)
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

        viewModel.listener = this

        viewModel.onViewReady()
    }

    override fun onWorkoutDataReady(workoutSummaryItemViewModels: List<WorkoutSummaryItemViewModel>) {
        val adapter = WorkoutSummaryAdapter(workoutSummaryItemViewModels)

        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.workoutSets.layoutManager = layoutManager
        binding.workoutSets.addItemDecoration(DividerItemDecoration(context, layoutManager.getOrientation()))
        binding.workoutSets.adapter = adapter
    }

    override fun onWorkAdded() {
        Toast.makeText(context, "Successfully added workout!!", Toast.LENGTH_SHORT).show()

        //figure out nav here
    }

    override fun onCancelAdd() {
        //figure out nav here
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
