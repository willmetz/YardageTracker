package com.slapshotapps.swimyardagetracker.ui.home

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.slapshotapps.swimyardagetracker.R
import com.slapshotapps.swimyardagetracker.databinding.HomeFragmentBinding
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class HomeFragment : Fragment(R.layout.home_fragment), HomeViewModel.HomeViewModelListener {

    private var binding: HomeFragmentBinding ? = null

    @Inject
    lateinit var viewModel: HomeViewModel

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this) // Providing the dependency, must call before super
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.setListener(this)

        binding?.item = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = HomeFragmentBinding.bind(view)
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
