package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.data.CivicsDataSource

//DONE: Create Factory to generate ElectionViewModel with provided election datasource
class ElectionsViewModelFactory(val arg: CivicsDataSource): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(CivicsDataSource::class.java)
            .newInstance(arg)
    }
}