package com.slapshotapps.swimyardagetracker.ui.records.editRecord

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.slapshotapps.swimyardagetracker.databinding.FragmentEditPersonalRecordBinding
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class EditPersonalRecordFragment : Fragment() {

    private var _binding: FragmentEditPersonalRecordBinding ? = null
    private val binding get() = _binding!!

    val args: EditPersonalRecordFragmentArgs by navArgs()

    @Inject
    lateinit var viewModel: EditPersonalRecordViewModel

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this) // Providing the dependency, must call before super
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditPersonalRecordBinding.inflate(inflater, container, false)

        viewModel.viewModelEvent.observe(viewLifecycleOwner, androidx.lifecycle.Observer { event -> onViewModelEvent(event) })

        // get the record
        viewModel.init(args.recordToEdit)

        return binding.root
    }

    private fun onViewModelEvent(event: EditPersonalRecordEvent) {
        when (event) {
            is EditPersonalRecordEvent.InitDisplay -> {
                updateDisplayData(event)
            }
            is EditPersonalRecordEvent.Loading -> {
                // nothing at this time, perhaps a spinner?
            }
            is EditPersonalRecordEvent.DeleteRecordError -> {
                Toast.makeText(requireContext(), event.msg, Toast.LENGTH_SHORT).show()
            }
            is EditPersonalRecordEvent.Success -> {
                Toast.makeText(requireContext(), event.msg, Toast.LENGTH_SHORT).show()
                NavHostFragment.findNavController(this).popBackStack()
            }
        }
    }

    private fun updateDisplayData(event: EditPersonalRecordEvent.InitDisplay) {
        binding.event.text = event.displayData.event
        if (event.displayData.yardageRecord != null) {
            val yardageData = event.displayData.yardageRecord
            binding.yardageRecordEnabled.isChecked = yardageData.enabled
            binding.yardageTimeEntry.setTime(yardageData.minutes, yardageData.seconds, yardageData.milliseconds)
            binding.yardageDateEntry.text = yardageData.formattedDate
        } else {
            binding.yardageRecordEnabled.isChecked = false
        }

        if (event.displayData.metricRecord != null) {
            val metricData = event.displayData.metricRecord
            binding.metricRecordEnabled.isChecked = metricData.enabled
            binding.metricTimeEntry.setTime(metricData.minutes, metricData.seconds, metricData.milliseconds)
            binding.metricDateEntry.text = metricData.formattedDate
        } else {
            binding.metricRecordEnabled.isChecked = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
