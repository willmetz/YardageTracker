package com.slapshotapps.swimyardagetracker.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.slapshotapps.swimyardagetracker.R
import com.slapshotapps.swimyardagetracker.databinding.HomeFragmentBinding
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class HomeFragment : Fragment(), HomeViewModel.HomeViewModelListener {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var binding: HomeFragmentBinding

    @Inject
    lateinit var viewModel: HomeViewModel

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this) // Providing the dependency, must call before super
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.setListener(this)

        binding.item = viewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        viewModel.onViewResume()
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
