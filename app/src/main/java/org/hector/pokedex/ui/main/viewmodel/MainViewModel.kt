package org.hector.pokedex.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import org.hector.pokedex.data.repository.MainRepository
import org.hector.pokedex.utils.Resource


class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {

    fun getPokemons(limit: Int, offset: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getPokemons(limit, offset)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}