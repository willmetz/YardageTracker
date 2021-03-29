package com.slapshotapps.swimyardagetracker.ui.records

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.slapshotapps.swimyardagetracker.repositories.PersonalRecordsRepository
import com.slapshotapps.swimyardagetracker.utils.StringProvider
import javax.inject.Inject

class PersonalRecordsViewModel @Inject constructor(
    private val personalRecordsRepository: PersonalRecordsRepository,
    private val stringProvider: StringProvider
) {
    val allRecords: LiveData<ArrayList<PersonalRecordItemViewModel>> =
            Transformations.map(personalRecordsRepository.getAllRecordsWithTimes()) {

                val records = ArrayList<PersonalRecordItemViewModel>()
                if (it.isNotEmpty()) {
                    it.forEach { personalRecordWithTimes ->
                        records.add(PersonalRecordItemViewModel(personalRecordWithTimes))
                    }

                    records.sortBy { it.mostRecentRecordDate }
                }

                records
    }

    private val _confirmDelete = MutableLiveData<DeleteRecord>()
    val confirmDelete: LiveData<DeleteRecord> = _confirmDelete

    fun onDelete(item: PersonalRecordItemViewModel) {
        val record = DeleteRecord("Are you sure you want to delete: ${item.event}", "Confirm Delete", item)
        _confirmDelete.postValue(record)
    }

    suspend fun onConfirmedDelete(item: PersonalRecordItemViewModel) {
        personalRecordsRepository.deleteRecord(item.recordID)
    }

    data class DeleteRecord(val msg: String, val title: String, val itemToDelete: PersonalRecordItemViewModel)
}
