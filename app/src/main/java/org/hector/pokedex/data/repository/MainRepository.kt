package org.hector.pokedex.data.repository

import org.hector.pokedex.data.api.ApiHelper

class MainRepository(private val apiHelper: ApiHelper) {

    suspend fun getPokemons(limit : Int, offset: Int) = apiHelper.getPokemons(limit, offset)
}