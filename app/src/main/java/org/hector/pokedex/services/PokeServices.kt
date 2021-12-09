package org.hector.pokedex.services

import org.hector.pokedex.model.PokemonDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeServices {
    @GET("pokemon/{id}/")
    fun getPokemon(@Path("id") id: Int): Call<PokemonDetail>
}