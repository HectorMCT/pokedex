package org.hector.pokedex.ui

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import org.hector.pokedex.R
import org.hector.pokedex.databinding.ActivityPokedetailBinding
import org.hector.pokedex.data.model.POKEMON_ID
import org.hector.pokedex.data.model.PokemonDetail
import org.hector.pokedex.data.model.Move
import org.hector.pokedex.data.api.ApiClient
import org.hector.pokedex.data.api.PokeServices
import org.hector.pokedex.data.api.StatService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

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
    }

    private fun obtenerDatosPokemon(pokemonID: Int) {

        //Construcción de la instancia de retrofit
        val retrofit = ApiClient.apiClient()

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
                        /*pokeResponse.moves.forEach {

                            callMoves(it.move.url.split("/")[6].toInt())

                        }*/
                        pokeResponse.name =  pokeResponse.name?.substring(0, 1)?.uppercase(Locale.getDefault()) + pokeResponse.name?.substring(1)
                        setUpUI(pokeResponse)
                        Log.e("POKE-STAT", "STATS ${pokeResponse}")
                    }
                } else{
                    Log.e("Not200","Error not 200: $response")
                }
            }

        })
    }

    private fun setUpUI(pokeResponse: PokemonDetail) {

        binding.pokeName.text = getString(R.string.poke_name,pokeResponse.order,pokeResponse.name)

        binding.peso.text = getString(R.string.peso, (pokeResponse.weight.toDouble()/10))
        binding.altura.text = getString(R.string.altura, (pokeResponse.height.toDouble()/10))

        binding.hp.text = getString(R.string.st_hp, pokeResponse.stats.find { it.stat.name == "hp" }?.baseStat!!)
        binding.at.text = getString(R.string.st_at, pokeResponse.stats.find { it.stat.name == "attack" }?.baseStat!!)
        binding.df.text = getString(R.string.st_df, pokeResponse.stats.find { it.stat.name == "defense" }?.baseStat!!)
        binding.sa.text = getString(R.string.st_sa, pokeResponse.stats.find { it.stat.name == "special-attack" }?.baseStat!!)
        binding.sd.text = getString(R.string.st_sd, pokeResponse.stats.find { it.stat.name == "special-defense" }?.baseStat!!)
        binding.sp.text = getString(R.string.st_sp, pokeResponse.stats.find { it.stat.name == "speed" }?.baseStat!!)

        binding.pokeStats.visibility = View.VISIBLE

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.determinateLinearIndicatorHp.setProgress(pokeResponse.stats[0].baseStat, true)
            binding.determinateLinearIndicatorAt.setProgress(pokeResponse.stats[1].baseStat, true)
            binding.determinateLinearIndicatorDf.setProgress(pokeResponse.stats[2].baseStat, true)
            binding.determinateLinearIndicatorSa.setProgress(pokeResponse.stats[3].baseStat, true)
            binding.determinateLinearIndicatorSd.setProgress(pokeResponse.stats[4].baseStat, true)
            binding.determinateLinearIndicatorSp.setProgress(pokeResponse.stats[5].baseStat, true)
        } else {
            binding.determinateLinearIndicatorHp.progress = pokeResponse.stats.find { it.stat.name == "hp" }?.baseStat!!
            binding.determinateLinearIndicatorAt.progress = pokeResponse.stats.find { it.stat.name == "attack" }?.baseStat!!
            binding.determinateLinearIndicatorDf.progress = pokeResponse.stats.find { it.stat.name == "defense" }?.baseStat!!
            binding.determinateLinearIndicatorSa.progress = pokeResponse.stats.find { it.stat.name == "special-attack" }?.baseStat!!
            binding.determinateLinearIndicatorSp.progress = pokeResponse.stats.find { it.stat.name == "special-defense" }?.baseStat!!
            binding.determinateLinearIndicatorSd.progress = pokeResponse.stats.find { it.stat.name == "speed" }?.baseStat!!
        }

        with(Glide.with(this@PokeDetailActivity)){
            load(pokeResponse.sprites.frontDefault).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.spriteFront)
            load(pokeResponse.sprites.frontShiny).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.spriteFrontShiny)
        }/*
        binding.shiny.visibility = View.VISIBLE
        binding.norm.visibility = View.VISIBLE*/
    }

    private fun callMoves(statID: Int){
        val retrofit = ApiClient.apiClient()
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
                        Log.d("STAT","STAT ID: ${pokeResponse.names.find { it.language.name==getString(
                            R.string.lenguage
                        ) }?.name}")
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
