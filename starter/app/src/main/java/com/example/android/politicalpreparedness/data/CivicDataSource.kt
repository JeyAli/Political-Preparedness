package com.example.android.politicalpreparedness.data

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.representative.model.Representative

interface CivicsDataSource {
    suspend fun refreshElectionsData()
    val electionsFollowed: LiveData<List<Election>>
    val electionsUpcoming: LiveData<List<Election>>
    suspend fun getRepresentatives(address: Address) : List<Representative>
    suspend fun getVoterInfo(election: Election): VoterInfoResponse
}