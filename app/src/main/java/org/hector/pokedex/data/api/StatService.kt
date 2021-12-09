package org.hector.pokedex.data.api

import org.hector.pokedex.data.model.Move
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface StatService {
    @GET("move/{id}/")
    fun getStat(@Path("id") id: Int): Call<Move>
}