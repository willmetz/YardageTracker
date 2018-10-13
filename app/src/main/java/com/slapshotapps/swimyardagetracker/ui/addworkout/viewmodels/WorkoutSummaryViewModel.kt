package com.slapshotapps.swimyardagetracker.ui.addworkout.viewmodels

import androidx.databinding.ObservableField


class WorkoutSummaryViewModel(private val listener:WorkoutSummaryListener) {

    interface WorkoutSummaryListener{
        fun onWorkoutDataReady(workoutSummaryItemViewModels: List<WorkoutSummaryItemViewModel>)
    }

    val workoutDateText = ObservableField<String>("")


    fun onViewReady(){
        //temp code until a db is added
        val testData = ArrayList<WorkoutSummaryItemViewModel>(4)

        val setOne = WorkoutSummaryItemViewModel("5", "100","Free", "yards")
        val setTwo = WorkoutSummaryItemViewModel("2", "75","Back", "yards")
        val setThree = WorkoutSummaryItemViewModel("12", "200","IM", "yards")
        val setFour = WorkoutSummaryItemViewModel("8", "50","Breast", "yards")

        testData.add(setOne)
        testData.add(setTwo)
        testData.add(setThree)
        testData.add(setFour)

        workoutDateText.set("Friday, October 12th 2018")

        listener.onWorkoutDataReady(testData)
    }
}