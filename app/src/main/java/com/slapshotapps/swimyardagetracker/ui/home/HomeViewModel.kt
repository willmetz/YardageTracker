package com.slapshotapps.swimyardagetracker.ui.home

import com.slapshotapps.swimyardagetracker.R
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutUoM
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutWithDetails
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutWithUoM
import com.slapshotapps.swimyardagetracker.repositories.WorkoutRepository
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface HomeState {
    object Loading : HomeState
    data class DataReady(
        val lastWorkoutDate: String,
        val lastWorkoutDistance: String,
        val yearWorkoutCount: String,
        val distanceForYear: String
    ) : HomeState
    data class NoData(val message: String) : HomeState
}

sealed interface HomeEffect {
    data class OnShowError(val messageId: Int) : HomeEffect
}

class HomeViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val ioDispatcher: CoroutineDispatcher
) {

    private val ioScope = CoroutineScope(ioDispatcher + SupervisorJob())

    interface HomeViewModelListener {
        fun onAddWorkout()
        fun onShowError(msgID: Int)
    }

    private val _homeScreenState: MutableStateFlow<HomeState> = MutableStateFlow(HomeState.Loading)
    val homeScreenState: StateFlow<HomeState> = _homeScreenState.asStateFlow()

    private val _homeScreenEffect: MutableSharedFlow<HomeEffect> = MutableSharedFlow()
    val homeScreenEffect: SharedFlow<HomeEffect> = _homeScreenEffect.asSharedFlow()

    private var disposables: CompositeDisposable ? = null
    private var homeScreenData: HomeScreenData ? = null

    private data class HomeScreenData(
        val workoutWithDetails: WorkoutWithDetails,
        val workoutCountForYear: Int,
        val workoutsForYear: List<WorkoutWithUoM>
    )

    fun onViewResume() {
        disposables = CompositeDisposable()

        if (_homeScreenState.value is HomeState.Loading) {
            val firstOfYear = Calendar.getInstance()
            firstOfYear.set(Calendar.MONTH, Calendar.JANUARY)
            firstOfYear.set(Calendar.DAY_OF_MONTH, 1)
            firstOfYear.set(Calendar.HOUR_OF_DAY, 0)
            firstOfYear.set(Calendar.MINUTE, 0)
            firstOfYear.set(Calendar.SECOND, 0)

            val disposable = Maybe.zip(workoutRepository.getMostRecentWorkoutWithDetails(),
                    workoutRepository.getWorkoutsCountSinceDate(firstOfYear.time),
                    workoutRepository.getAllWorkoutsWithUoMSinceDate(firstOfYear.time)
            ) { workoutDetails, workoutCount, workouts ->
                HomeScreenData(workoutDetails, workoutCount, workouts)
            }
                .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::dataReady,
                            this::errorRetrievingLatestWorkout)

            disposables?.add(disposable)
        }
    }

    fun onViewDestoyed() {
        disposables?.dispose()
        ioScope.cancel()
    }

    private fun dataReady(homeScreenData: HomeScreenData) {
        this.homeScreenData = homeScreenData

        ioScope.launch {
            if (homeScreenData.workoutWithDetails.workoutSets.isEmpty()) {
                _homeScreenEffect.emit(HomeEffect.OnShowError(R.string.no_workouts))
            }
        }

        processWorkout(homeScreenData.workoutWithDetails, homeScreenData)
    }

    private fun errorRetrievingLatestWorkout(throwable: Throwable) {
        // TODO
        homeScreenData = null
        ioScope.launch {
            _homeScreenEffect.emit(HomeEffect.OnShowError(R.string.no_workouts))
        }
    }

    private fun processWorkout(workoutWithDetails: WorkoutWithDetails?, homeScreenData: HomeScreenData) {
        if (workoutWithDetails == null) {
            _homeScreenState.value = HomeState.NoData("")
        } else {
            var distance = 0
            workoutWithDetails.workoutSets.forEach { distance += it.distance * it.reps }

            val workoutDistance = String.format(Locale.US, "%d %s", distance, workoutWithDetails.workout.uoM.toString())
            val workoutDate = workoutWithDetails.workout.getFormattedWorkoutDate()
            val workoutCount = homeScreenData.workoutCountForYear.toString()
            val yardageForYear = getYearsYardage(homeScreenData.workoutsForYear).toString()
            _homeScreenState.value = HomeState.DataReady(workoutDate, workoutDistance, workoutCount, yardageForYear)
        }
    }

    private fun getYearsYardage(workoutOuts: List<WorkoutWithUoM>): Int {
        var yardage = 0
        val METERS_TO_YARDS = 1.09361

        for (workout in workoutOuts) {
            when (workout.uoM) {
                WorkoutUoM.METERS -> yardage += (workout.distance * workout.reps * METERS_TO_YARDS).roundToInt()
                WorkoutUoM.YARDS -> yardage += workout.distance * workout.reps
                else -> Unit
            }
        }

        return yardage
    }
}
