package org.hector.pokedex.data.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    const val SPRITE_URL: String = "https://img.pokemondb.net/sprites/black-white/anim/normal/"
    private const val BASE_URL:String = "https://pokeapi.co/api/v2/"
    private const val TIMEOUT_CALL_SECONDS = 1L

    fun apiClient(): Retrofit {

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)


        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(TIMEOUT_CALL_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_CALL_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_CALL_SECONDS, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    }

}