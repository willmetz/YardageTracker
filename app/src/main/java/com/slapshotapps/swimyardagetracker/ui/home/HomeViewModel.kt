package com.slapshotapps.swimyardagetracker.ui.home

import androidx.databinding.ObservableField
import com.slapshotapps.swimyardagetracker.R
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutWithDetails
import com.slapshotapps.swimyardagetracker.repositories.WorkoutRepository
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
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
    private var homeScreenData: HomeScreenData ?= null
    val lastWorkoutDistance = ObservableField<String>("N/A")
    val lastWorkoutDate = ObservableField<String>("N/A")
    val yearWorkoutCount = ObservableField<String>("0")

    private data class HomeScreenData(val workoutWithDetails: WorkoutWithDetails, val workoutsForYear: Int)

    fun onViewResume() {
        disposables = CompositeDisposable()

        if(homeScreenData == null){
            val firstOfYear = Calendar.getInstance()
            firstOfYear.set(Calendar.MONTH, Calendar.JANUARY)
            firstOfYear.set(Calendar.DAY_OF_MONTH, 1)
            firstOfYear.set(Calendar.HOUR_OF_DAY, 0)
            firstOfYear.set(Calendar.MINUTE, 0)
            firstOfYear.set(Calendar.SECOND, 0)


            val disposable = Maybe.zip(workoutRepository.getMostRecentWorkoutWithDetails(),
                    workoutRepository.getWorkoutsCountSinceDate(firstOfYear.time),
                    BiFunction<WorkoutWithDetails, Int, HomeScreenData>{ workoutDetails, workoutCount ->
                        HomeScreenData(workoutDetails, workoutCount)
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::dataReady,
                            this::errorRetrievingLatestWorkout)

            disposables?.add(disposable)
        }
    }

    fun onViewDestoyed(){
        disposables?.dispose()
    }

    private fun dataReady(homeScreenData: HomeScreenData){
        this.homeScreenData = homeScreenData

        if(homeScreenData.workoutWithDetails.workoutSets.isEmpty()){
            listener?.onShowError(R.string.no_workouts)
        }


        updateLastWorkoutDistance(homeScreenData.workoutWithDetails)
        updateLastWorkoutDate(homeScreenData.workoutWithDetails)
        updateTotalWorkoutsForYear(homeScreenData.workoutsForYear)
    }

    private fun errorRetrievingLatestWorkout(throwable: Throwable){
        //TODO
        homeScreenData = null
    }

    fun setListener(listener: HomeViewModelListener) {
        this.listener = listener
    }

    private fun updateLastWorkoutDistance(workoutWithDetails: WorkoutWithDetails?) {

        if (workoutWithDetails == null) {
            lastWorkoutDistance.set("N/A")
        }else{
            //add up workout distances
            var distance = 0
            workoutWithDetails.workoutSets.forEach { distance += it.distance }

            val workoutDistance = String.format(Locale.US, "%d %s", distance, workoutWithDetails.workout.uoM.toString())

            lastWorkoutDistance.set(workoutDistance)
        }
    }

    private fun updateLastWorkoutDate(workoutWithDetails: WorkoutWithDetails?) {
        if(workoutWithDetails == null){
            lastWorkoutDate.set("N/A")
        }else{
            lastWorkoutDate.set(workoutWithDetails.workout.getFormattedWorkoutDate())
        }
    }

    private fun updateTotalWorkoutsForYear(workoutCount: Int) {
        yearWorkoutCount.set(workoutCount.toString())
    }

    fun totalYardageForYear(): String {
        return "5000"
    }

    fun onAddWorkoutTapped() {
        listener?.onAddWorkout()
    }
}


