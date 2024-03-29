package com.slapshotapps.swimyardagetracker.ui.records

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.slapshotapps.swimyardagetracker.repositories.PersonalRecordsRepository
import com.slapshotapps.swimyardagetracker.utils.EventWrapper
import com.slapshotapps.swimyardagetracker.utils.StringProvider
import javax.inject.Inject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.map

class PersonalRecordsViewModel @Inject constructor(
    private val personalRecordsRepository: PersonalRecordsRepository,
    private val stringProvider: StringProvider,
    private val ioDispatcher: CoroutineDispatcher
) {

    private val ioScope = CoroutineScope(ioDispatcher + SupervisorJob())

    val allRecords: LiveData<ArrayList<PersonalRecordItemViewModel>> =
        personalRecordsRepository.getAllRecordsWithTimes().map {
            val records = ArrayList<PersonalRecordItemViewModel>()
            if (it.isNotEmpty()) {
                it.forEach { personalRecordWithTimes ->
                    records.add(PersonalRecordItemViewModel(personalRecordWithTimes))
                }

                records.sortBy { it.mostRecentRecordDate }
            }

            records
        }.asLiveData()

    private val _confirmDelete = MutableLiveData<EventWrapper<DeleteRecord>>()
    val confirmDelete: LiveData<EventWrapper<DeleteRecord>> = _confirmDelete

    fun onDelete(item: PersonalRecordItemViewModel) {
        val record = DeleteRecord("Are you sure you want to delete: ${item.event}", "Confirm Delete", item)
        _confirmDelete.postValue(EventWrapper(record))
    }

    fun onConfirmedDelete(item: PersonalRecordItemViewModel) {
        ioScope.launch {
            personalRecordsRepository.deleteRecord(item.recordID)
        }
    }

    fun onDestroy() {
        ioScope.cancel()
    }

    data class DeleteRecord(val msg: String, val title: String, val itemToDelete: PersonalRecordItemViewModel)
}
