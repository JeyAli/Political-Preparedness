package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.data.CivicsDataSource
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch

//DONE: Construct ViewModel and provide election datasource
class ElectionsViewModel(dataSource: CivicsDataSource): ViewModel() {

    val upcomingElections = dataSource.electionsUpcoming
    val savedElections = dataSource.electionsFollowed
    //DONE: Create live data val for saved elections

    init{
        viewModelScope.launch{
            dataSource.refreshElectionsData()
        }
    }
    //DONE: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database

    //DONE: Create functions to navigate to saved or upcoming election voter info

}