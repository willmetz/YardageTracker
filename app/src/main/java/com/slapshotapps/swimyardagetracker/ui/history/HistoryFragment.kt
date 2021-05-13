package com.slapshotapps.swimyardagetracker.ui.history

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.slapshotapps.swimyardagetracker.R
import com.slapshotapps.swimyardagetracker.databinding.FragmentHistoryBinding
import com.slapshotapps.swimyardagetracker.repositories.WorkoutRepository
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment(R.layout.fragment_history) {

    @Inject
    lateinit var repository: WorkoutRepository

    @Inject
    lateinit var viewModel: HistoryViewModel

    private var binding: FragmentHistoryBinding ? = null

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this) // Providing the dependency, must call before super
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHistoryBinding.bind(view)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding?.historyList?.layoutManager = layoutManager
        binding?.historyList?.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))

        viewModel.allWorkouts.observe(viewLifecycleOwner, Observer {

            if (it.isEmpty()) {
                binding?.noHistoryView?.visibility = View.VISIBLE
            } else {
                binding?.noHistoryView?.visibility = View.GONE
                binding?.historyList?.adapter = WorkoutHistoryAdapter(it)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
