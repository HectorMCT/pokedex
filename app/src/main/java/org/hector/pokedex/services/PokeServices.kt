package org.hector.pokedex.services

import org.hector.pokedex.model.PokeList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PokeServices {

    @GET("pokemon")
    fun getPokemon(@Query("limit") limit: Int, @Query("offset") offset: Int): Call<PokeList>

}