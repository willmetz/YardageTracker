package com.slapshotapps.swimyardagetracker.ui.records

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.slapshotapps.swimyardagetracker.R
import com.slapshotapps.swimyardagetracker.ui.history.HistoryViewModel
import kotlinx.android.synthetic.main.fragment_personal_records.view.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [PersonalRecordsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PersonalRecordsFragment : Fragment() {

    @Inject
    lateinit var viewModel: PersonalRecordsViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_personal_records, container, false)

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val decoration = DividerItemDecoration(context, layoutManager.orientation)
        val drawable = ContextCompat.getDrawable(context!!, R.drawable.transparent_spacer)
        if(drawable != null){
            decoration.setDrawable(drawable)
        }
        view.records_list.layoutManager = layoutManager
        view.records_list.addItemDecoration(decoration)

        viewModel.allRecords.observe(viewLifecycleOwner, Observer{
            if(it.count() > 0){
                view.records_list.adapter = PersonalRecordsItemAdapter(it, this::onEditRecordSelected)
            }
        })

        view.add_new_record.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.action_personalRecordsFragment_to_personalRecordCrudFragment)
        }

        return view
    }

    private fun onEditRecordSelected(item: PersonalRecordItemViewModel){
        //TODO, need to pass the data
        NavHostFragment.findNavController(this).navigate(R.id.action_personalRecordsFragment_to_personalRecordCrudFragment)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                PersonalRecordsFragment()
    }
}