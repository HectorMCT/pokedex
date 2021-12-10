package org.hector.pokedex.data.api

class ApiHelper(private val apiService: PokeListServices) {

    suspend fun getPokemons(limit : Int, offset: Int) = apiService.getPokemon(limit, offset)
}