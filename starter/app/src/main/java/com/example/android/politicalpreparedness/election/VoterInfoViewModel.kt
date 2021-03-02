package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch

class VoterInfoViewModel(application: Application) : AndroidViewModel(application) {

    private val database = ElectionDatabase.getInstance(application)
    private val electionsRepository = ElectionsRepository(database)

    val voterInfo = electionsRepository.voterInfo

    var webUrl = MutableLiveData<String>()

    private val electionId = MutableLiveData<Int>()
    val election = Transformations.switchMap(electionId) {
            id -> electionsRepository.getElection(id) }

    fun getElection(id: Int) {
        electionId.value = id
    }

    fun getVoterInfo(electionId: Int, address: String) =
        viewModelScope.launch {
            electionsRepository.getVoterInfo(electionId, address)
        }

    fun saveElection(election: Election) {
        election.isFollowed = !election.isFollowed
        viewModelScope.launch {
            electionsRepository.insertElection(election)
        }
    }

    fun openWebUrl(url: String) {
        webUrl.value = url
    }
}