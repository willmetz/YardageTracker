package com.slapshotapps.swimyardagetracker.ui.home

import com.slapshotapps.swimyardagetracker.models.workout.WorkoutWithDetails
import com.slapshotapps.swimyardagetracker.repositories.WorkoutRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject


class HomeViewModel @Inject constructor(private val workoutRepository: WorkoutRepository) {

    interface HomeViewModelListener {
        fun onAddWorkout()
        fun onShowError(msgID: Int)
    }

    private var listener: HomeViewModelListener? = null
    private var disposables: CompositeDisposable ?= null
    private var lastWorkoutDetails: WorkoutWithDetails ?= null

    fun onResume() {
        disposables = CompositeDisposable()

        if(lastWorkoutDetails == null){
            val disposable = workoutRepository.getMostRecentWorkoutWithDetails()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onFoundLastWorkoutDetails,
                            this::errorRetrievingLatestWorkout,
                            this::noWorkoutFound)

            disposables?.add(disposable)
        }
    }

    private fun onFoundLastWorkoutDetails(workoutWithDetails: WorkoutWithDetails){
        lastWorkoutDetails = workoutWithDetails
    }

    private fun errorRetrievingLatestWorkout(throwable: Throwable){
        //TODO
        lastWorkoutDetails = null
    }

    private fun noWorkoutFound() {
        //TODO
        lastWorkoutDetails = null
    }

    fun setListener(listener: HomeViewModelListener) {
        this.listener = listener
    }

    fun lastWorkoutYardage(): String {
        val lastRetrievedWorkout = lastWorkoutDetails

        if (lastRetrievedWorkout == null) {
            return "N/A"
        }
        //add up workout distances
        var distance = 0
        lastRetrievedWorkout.workoutSets.forEach { distance += it.distance }

        return String.format(Locale.US, "%d %s", distance, lastRetrievedWorkout.workout.uoM.toString())

    }

    fun lastWorkoutDate(): String {
        val lastRetrievedWorkout = lastWorkoutDetails

        if(lastRetrievedWorkout == null){
            return "N/A"
        }

        return lastRetrievedWorkout.workout.getFormattedWorkoutDate()
    }

    fun totalWorkoutsForYear(): String {
        return "8"
    }

    fun totalYardageForYear(): String {
        return "5000"
    }

    fun onAddWorkoutTapped() {
        listener?.onAddWorkout()
    }
}


