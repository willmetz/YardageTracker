package com.slapshotapps.swimyardagetracker.ui.records

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.slapshotapps.swimyardagetracker.R
import com.slapshotapps.swimyardagetracker.databinding.FragmentPersonalRecordsBinding
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_personal_records.*
import kotlinx.android.synthetic.main.fragment_personal_records.view.*

/**
 * A simple [Fragment] subclass.
 * Use the [PersonalRecordsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PersonalRecordsFragment : Fragment() {

    @Inject
    lateinit var viewModel: PersonalRecordsViewModel

    private var _binding: FragmentPersonalRecordsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this) // Providing the dependency, must call before super
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPersonalRecordsBinding.inflate(inflater, container, false)

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val decoration = DividerItemDecoration(context, layoutManager.orientation)
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.transparent_spacer)
        if (drawable != null) {
            decoration.setDrawable(drawable)
        }

        binding.recordsList.layoutManager = layoutManager
        binding.recordsList.addItemDecoration(decoration)

        viewModel.allRecords.observe(viewLifecycleOwner, Observer {
            onListChanged(it)
        })

        binding.addNewRecord.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.action_personalRecordsFragment_to_addNewPersonalRecordFragment)
        }

        return binding.root
    }

    private fun onListChanged(items: List<PersonalRecordItemViewModel>) {

        // TODO - not best to renew the adapter as you lose scroll position, switch this over to the DiffUtil instead
        if (items.count() > 0) {
            binding.noRecords.visibility = View.GONE
            binding.recordsList.visibility = View.VISIBLE
            binding.recordsList.adapter = PersonalRecordsItemAdapter(items, this::onEditRecordSelected)
        } else {
            binding.noRecords.visibility = View.VISIBLE
            binding.recordsList.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onEditRecordSelected(item: PersonalRecordItemViewModel) {
        val action = PersonalRecordsFragmentDirections.actionEditPersonalRecord(item.getYardageTrackerRecord())
        NavHostFragment.findNavController(this).navigate(action)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                PersonalRecordsFragment()
    }
}
