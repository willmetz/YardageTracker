package com.slapshotapps.swimyardagetracker.ui.records.crud

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.slapshotapps.swimyardagetracker.R
import com.slapshotapps.swimyardagetracker.databinding.FragmentPersonalRecordCrudBinding
import com.slapshotapps.swimyardagetracker.databinding.FragmentPersonalRecordsBinding
import com.slapshotapps.swimyardagetracker.ui.records.PersonalRecordsViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [PersonalRecordCrudFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PersonalRecordCrudFragment : Fragment() {

    private var _binding: FragmentPersonalRecordCrudBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModel: PersonalRecordCrudViewModel


    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this) // Providing the dependency, must call before super
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentPersonalRecordCrudBinding.inflate( inflater, container, false)


        binding.save.setOnClickListener{
            GlobalScope.launch{
                //TODO add data
                viewModel.onAddNewRecord()
            }
        }
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}