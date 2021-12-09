package org.hector.pokedex.data.api

import org.hector.pokedex.data.model.PokeList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PokeListServices {

    @GET("pokemon")
    fun getPokemon(@Query("limit") limit: Int, @Query("offset") offset: Int): Call<PokeList>

}