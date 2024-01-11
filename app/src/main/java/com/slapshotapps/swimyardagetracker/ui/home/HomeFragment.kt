package com.slapshotapps.swimyardagetracker.ui.home

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import com.slapshotapps.swimyardagetracker.R
import com.slapshotapps.swimyardagetracker.databinding.HomeFragmentBinding
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.home_fragment), HomeViewModel.HomeViewModelListener {

    private var binding: HomeFragmentBinding ? = null

    @Inject
    lateinit var viewModel: HomeViewModel

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this) // Providing the dependency, must call before super
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = HomeFragmentBinding.bind(view)

        binding?.addWorkout?.setOnClickListener {
            onAddWorkout()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.homeScreenState.collectLatest {
                    when (it) {
                        is HomeState.DataReady -> {
                            binding?.totalYardage?.text = it.distanceForYear
                            binding?.workoutCount?.text = it.yearWorkoutCount
                            binding?.yardage?.text = it.lastWorkoutDistance
                            binding?.workoutDate?.text = it.lastWorkoutDate
                        }
                        HomeState.Loading -> Unit
                        is HomeState.NoData -> {
                            binding?.totalYardage?.text = getString(R.string.no_workout_data)
                            binding?.workoutCount?.text = getString(R.string.no_workout_data)
                            binding?.yardage?.text = getString(R.string.no_workout_data)
                            binding?.workoutDate?.text = getString(R.string.no_workout_data)
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.homeScreenEffect.collectLatest {
                    when (it) {
                        is HomeEffect.OnShowError -> onShowError(it.messageId)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.onViewResume()
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }

    override fun onStop() {
        super.onStop()

        viewModel.onViewDestoyed()
    }

    override fun onAddWorkout() {
        NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_workoutDateFragment)
    }

    override fun onShowError(msgID: Int) {
        Toast.makeText(context, msgID, Toast.LENGTH_SHORT).show()
    }
}
