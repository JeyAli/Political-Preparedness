package com.example.android.politicalpreparedness.representative

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel(application: Application): ViewModel() {

    val representatives: MutableLiveData<List<Representative>> = MutableLiveData()
    val address = MutableLiveData<Address>()

    fun getRepresentatives() {
        if (address.value != null) {
            viewModelScope.launch {
                try {
                    val (offices, officials) = CivicsApi.retrofitService.getRepresentatives(
                        address.value!!.toFormattedString()
                    ).await()
                    representatives.postValue(offices.flatMap { office ->
                        office.getRepresentatives(
                            officials
                        )
                    })
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }
}