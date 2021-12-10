package org.hector.pokedex.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.hector.pokedex.data.api.ApiHelper
import org.hector.pokedex.data.repository.MainRepository
import org.hector.pokedex.ui.main.viewmodel.MainViewModel

class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(MainRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}