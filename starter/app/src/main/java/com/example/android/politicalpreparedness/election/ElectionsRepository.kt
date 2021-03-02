package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception

class ElectionsRepository(private val database: ElectionDatabase) {

    val elections: LiveData<List<Election>> = database.electionDao.getElections()

    val voterInfo = MutableLiveData<VoterInfoResponse>()
    val representatives = MutableLiveData<RepresentativeResponse>()

    suspend fun updateElections() {
        try {
            withContext(Dispatchers.IO) {
                val electionsResponse = CivicsApi.retrofitService.getElections().execute();
                if (electionsResponse.isSuccessful) {
                    database.electionDao.insertElections(electionsResponse.body()!!.elections);
                } else {
                    Timber.e(electionsResponse.errorBody()!!.string());
                }
                //database.electionDao.insertElections(electionsResponse.elections)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getElection(id: Int) = database.electionDao.getElection(id)

    suspend fun getVoterInfo(electionId: Int, address: String) {
        try {
            withContext(Dispatchers.IO) {
                val response =
                    CivicsApi.retrofitService.getVoterInfo(address, electionId).await()
                voterInfo.postValue(response)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun insertElection(election: Election) {
        Timber.i(election.isFollowed.toString())
        withContext(Dispatchers.IO) {
            database.electionDao.insertElection(election)
        }
    }

    val savedElections: LiveData<List<Election>> = database.electionDao.getFollowedElections()
}