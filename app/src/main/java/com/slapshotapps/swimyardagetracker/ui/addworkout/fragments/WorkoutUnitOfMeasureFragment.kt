package com.slapshotapps.swimyardagetracker.ui.addworkout.fragments


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import com.slapshotapps.swimyardagetracker.R
import com.slapshotapps.swimyardagetracker.databinding.FragmentWorkoutUnitOfMeasureBinding
import com.slapshotapps.swimyardagetracker.ui.addworkout.viewmodels.WorkoutUoMViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [WorkoutUnitOfMeasure.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class WorkoutUnitOfMeasure : Fragment(), WorkoutUoMViewModel.WorkoutViewModelUoMListener {

    lateinit var binding: FragmentWorkoutUnitOfMeasureBinding
    lateinit var viewModel: WorkoutUoMViewModel

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        viewModel = ViewModelProviders.of(this).get(WorkoutUoMViewModel::class.java)
        viewModel.listener = this
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_workout_unit_of_measure, container, false)

        binding.item = viewModel

        return binding.root
    }


    override fun onAddWorkouts() {
        NavHostFragment.findNavController(this).navigate(R.id.action_workoutUnitOfMeasure_to_workoutSetFragment)
    }

    override fun onChangeDate() {
        //TODO
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment
         *
         * @return A new instance of fragment WorkoutUnitOfMeasure.
         */
        @JvmStatic
        fun newInstance() =
                WorkoutUnitOfMeasure()
    }
}
