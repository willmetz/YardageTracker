package com.slapshotapps.swimyardagetracker.ui.addworkout.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.slapshotapps.swimyardagetracker.R


/**
 * A simple [Fragment] subclass.
 * Use the [WorkoutUnitOfMeasure.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class WorkoutUnitOfMeasure : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_workout_unit_of_measure, container, false)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment
         *
         * @return A new instance of fragment WorkoutUnitOfMeasure.
         */
        @JvmStatic
        fun newInstance() =
                WorkoutUnitOfMeasure()
    }
}
