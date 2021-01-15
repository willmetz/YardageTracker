package com.slapshotapps.swimyardagetracker.ui.history


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.slapshotapps.swimyardagetracker.R
import com.slapshotapps.swimyardagetracker.repositories.WorkoutRepository
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_history.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment() {

    @Inject
    lateinit var repository: WorkoutRepository

    @Inject
    lateinit var viewModel: HistoryViewModel


    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this) // Providing the dependency, must call before super
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val historyList = view.findViewById<RecyclerView>(R.id.history_list)

        historyList.layoutManager = layoutManager
        historyList.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))

        viewModel.allWorkouts.observe(viewLifecycleOwner, Observer {

            if(it.isEmpty()){
                no_history_view.visibility = View.VISIBLE;
            }else{
                no_history_view.visibility = View.GONE;
                history_list.adapter = WorkoutHistoryAdapter(it)
            }

        })

        return view
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment.
         *
         * @return A new instance of fragment HistoryFragment.
         */
        @JvmStatic
        fun newInstance() =
                HistoryFragment()
    }
}
