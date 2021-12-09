package org.hector.pokedex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import org.hector.pokedex.databinding.ActivityPokedetailBinding
import org.hector.pokedex.model.POKEMON_ID
import org.hector.pokedex.model.PokemonDetail
import org.hector.pokedex.model.Move
import org.hector.pokedex.services.PokeServices
import org.hector.pokedex.services.StatService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PokeDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPokedetailBinding
    private val moves = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokedetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pokeID :Int = intent.getIntExtra(POKEMON_ID, -1)
        Toast.makeText(this, "POKEMON - URL: $pokeID", Toast.LENGTH_SHORT).show()
        obtenerDatosPokemon(pokeID)
        //Log.d("STAT","STATS: $moves")
    }

    private fun obtenerDatosPokemon(pokemonID: Int) {

        //Construcción de la instancia de retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val endpoint = retrofit.create(PokeServices::class.java)

        val pokeCall = endpoint.getPokemon(pokemonID)

        pokeCall.enqueue(object : Callback<PokemonDetail> {
            //imprimimos algo si no nos llegó respuesta
            override fun onFailure(call: Call<PokemonDetail>, t: Throwable) {
                Log.e("Error","Error: $t")
            }

            //mostramos los archivos solo si el resultado es 200
            override fun onResponse(call: Call<PokemonDetail>, response: Response<PokemonDetail>) {
                if(response.isSuccessful) {
                    val pokeResponse = response.body()
                    if (pokeResponse != null) {
                        //Log.e("POKEMON","ID: ${pokeResponse.stats}")
                        pokeResponse.moves.forEach {

                            callMoves(it.move.url.split("/")[6].toInt())

                        }
                        //Log.d("MOVES","MOVES: ${pokeResponse.moves}")
                        setUpUI(pokeResponse)
                    }
                } else{
                    Log.e("Not200","Error not 200: $response")
                }
            }

        })
    }

    private fun setUpUI(pokeResponse: PokemonDetail) {

        with(Glide.with(this@PokeDetailActivity)){
            load(pokeResponse.sprites.frontDefault).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.spriteFront)
            load(pokeResponse.sprites.backDefault).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.spriteBack)
            load(pokeResponse.sprites.backShiny).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.spriteBackShiny)
            load(pokeResponse.sprites.frontShiny).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.spriteFrontShiny)
        }
    }

    private fun callMoves(statID: Int){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val endpoint = retrofit.create(StatService::class.java)
        val pokeCall = endpoint.getStat(statID)
        pokeCall.enqueue(object : Callback<Move> {
            //imprimimos algo si no nos llegó respuesta
            override fun onFailure(call: Call<Move>, t: Throwable) {
                Log.e("Error","Error: $t")
            }

            //mostramos los archivos solo si el resultado es 200
            override fun onResponse(call: Call<Move>, response: Response<Move>) {
                if(response.isSuccessful) {
                    val pokeResponse = response.body()
                    if (pokeResponse != null) {
                        Log.d("STAT","STAT ID: ${pokeResponse.names.find { it.language.name==getString(R.string.lenguage) }?.name}")
                        pokeResponse.names.find { it.language.name==getString(R.string.lenguage) }?.name?.let {
                            moves.add(it)
                        }
                    }
                } else{
                    Log.d("Not200","Error not 200: $response")
                }
            }

        })
    }
}
