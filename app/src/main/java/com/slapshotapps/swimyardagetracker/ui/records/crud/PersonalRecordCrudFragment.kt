package com.slapshotapps.swimyardagetracker.ui.records.crud

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.slapshotapps.swimyardagetracker.R
import com.slapshotapps.swimyardagetracker.databinding.FragmentPersonalRecordCrudBinding
import com.slapshotapps.swimyardagetracker.databinding.FragmentPersonalRecordsBinding

/**
 * A simple [Fragment] subclass.
 * Use the [PersonalRecordCrudFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PersonalRecordCrudFragment : Fragment() {

    private var _binding: FragmentPersonalRecordCrudBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentPersonalRecordCrudBinding.inflate( inflater, container, false)


        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}