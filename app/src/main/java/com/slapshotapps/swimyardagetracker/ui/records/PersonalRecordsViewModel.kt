package com.slapshotapps.swimyardagetracker.ui.records

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.slapshotapps.swimyardagetracker.repositories.PersonalRecordsRepository
import javax.inject.Inject

class PersonalRecordsViewModel @Inject constructor(private val personalRecordsRepository: PersonalRecordsRepository){
    val allRecords : LiveData<ArrayList<PersonalRecordItemViewModel>> =
            Transformations.map(personalRecordsRepository.getAllRecordsWithTimes()){

                val records = ArrayList<PersonalRecordItemViewModel>()
                if(it.isNotEmpty()){
                    it.forEach { personalRecordWithTimes ->
                        records.add(PersonalRecordItemViewModel(personalRecordWithTimes))
                    }

                    records.sortBy { it.mostRecentRecordDate }
                }

                records
    }

}


