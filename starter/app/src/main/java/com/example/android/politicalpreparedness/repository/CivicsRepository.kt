package com.example.android.politicalpreparedness.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.android.politicalpreparedness.data.CivicsDataSource
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.toDatabaseEntity
import com.example.android.politicalpreparedness.database.toDomainModel
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class CivicsRepository(
    private val electionDao: ElectionDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO): CivicsDataSource {

    override val electionsFollowed: LiveData<List<Election>> =
        Transformations.map(electionDao.getFollowedElections()) {
            it.toDomainModel()
        }

    override val electionsUpcoming: LiveData<List<Election>> =
        Transformations.map((electionDao.selectAllElections())) {
            it.toDomainModel()
        }

    override suspend fun refreshElectionsData() {
        withContext(Dispatchers.IO) {
            try {
                val response = CivicsApi.retrofitService.getElections().await()
                val elections = response.elections
                electionDao.insertAll(*elections.toDatabaseEntity())
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Timber.d("Refresh failed ${e.message}")
                }
                e.printStackTrace()
            }
        }
    }

    override suspend fun getRepresentatives(address: Address): List<Representative> {
            try {
                val response =
                    CivicsApi.retrofitService.getRepresentatives(address.toFormattedString())
                        .await()
                var representatives = mutableListOf<Representative>()

                for (i in 0 until response.offices.size) {
                    representatives.add(Representative(response.officials[i], response.offices[i]))
                }

                return representatives
            }
            catch (exception: java.lang.Exception){
                Log.e("CivicsRepository",exception.message!!)

                throw exception
            }
    }

    override suspend fun getVoterInfo(election: Election): VoterInfoResponse {
        try{
            val voterInfo = CivicsApi.retrofitService.getVoterInfo(election.division.toFormattedString(), election.id).await()

            return voterInfo
        }
        catch (exception: Exception){
            Log.e("CivicsRepository",exception.message!!)

            throw exception
        }
    }
}