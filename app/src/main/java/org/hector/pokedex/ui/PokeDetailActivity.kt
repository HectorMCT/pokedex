package org.hector.pokedex.ui

import android.content.res.Resources
import android.graphics.drawable.GradientDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.skydoves.rainbow.colorArray
import com.skydoves.rainbow.rainbow
import org.hector.pokedex.R
import org.hector.pokedex.databinding.ActivityPokedetailBinding
import org.hector.pokedex.data.model.POKEMON_ID
import org.hector.pokedex.data.model.PokemonDetail
import org.hector.pokedex.data.api.ApiClient
import org.hector.pokedex.data.api.PokeServices
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
                        pokeResponse.name =  pokeResponse.name?.substring(0, 1)?.uppercase(Locale.getDefault()) + pokeResponse.name?.substring(1)
                        setUpUI(pokeResponse)
                        Log.e("POKE-STAT", "STATS $pokeResponse")
                    }
                } else{
                    Log.e("Not200","Error not 200: $response")
                }
            }

        })
    }

    private fun setUpUI(pokeResponse: PokemonDetail) {

        with(binding){
            if (pokeResponse.types.size == 1) {
                val gradientDrawable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        intArrayOf(getColor(getTypeColor(pokeResponse.types[0].type.name)),
                            getColor(getTypeColor(pokeResponse.types[0].type.name)))
                    )
                } else {
                    GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        intArrayOf(
                            getColor(this@PokeDetailActivity,getTypeColor(pokeResponse.types[0].type.name)),
                            getColor(this@PokeDetailActivity,getTypeColor(pokeResponse.types[0].type.name)))
                    )
                }
                gradientDrawable.cornerRadius = 0f
                pokeLayout.background = gradientDrawable
            } else if (pokeResponse.types.size == 2) {
                val gradientDrawable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        intArrayOf(getColor(getTypeColor(pokeResponse.types[0].type.name)),
                            getColor(getTypeColor(pokeResponse.types[1].type.name)))
                    )
                } else {
                    GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        intArrayOf(
                            getColor(this@PokeDetailActivity,getTypeColor(pokeResponse.types[0].type.name)),
                            getColor(this@PokeDetailActivity,getTypeColor(pokeResponse.types[1].type.name))
                        )
                    )
                }
                pokeLayout.background = gradientDrawable
            }
        }

        with(binding) {
            pokeName.text =
                getString(R.string.poke_name, pokeResponse.order, pokeResponse.name)

            peso.text = getString(R.string.peso, (pokeResponse.weight.toDouble() / 10))
            altura.text = getString(R.string.altura, (pokeResponse.height.toDouble() / 10))

            hp.text = getString(
                R.string.st_hp,
                pokeResponse.stats.find { it.stat.name == "hp" }?.baseStat!!
            )
            at.text = getString(
                R.string.st_at,
                pokeResponse.stats.find { it.stat.name == "attack" }?.baseStat!!
            )
            df.text = getString(
                R.string.st_df,
                pokeResponse.stats.find { it.stat.name == "defense" }?.baseStat!!
            )
            sa.text = getString(
                R.string.st_sa,
                pokeResponse.stats.find { it.stat.name == "special-attack" }?.baseStat!!
            )
            sd.text = getString(
                R.string.st_sd,
                pokeResponse.stats.find { it.stat.name == "special-defense" }?.baseStat!!
            )
            sp.text = getString(
                R.string.st_sp,
                pokeResponse.stats.find { it.stat.name == "speed" }?.baseStat!!
            )

            pokeStats.visibility = View.VISIBLE

            dliHp.progress =
                pokeResponse.stats.find { it.stat.name == "hp" }?.baseStat!!.toFloat()
            dliAt.progress =
                pokeResponse.stats.find { it.stat.name == "attack" }?.baseStat!!.toFloat()
            dliDf.progress =
                pokeResponse.stats.find { it.stat.name == "defense" }?.baseStat!!.toFloat()
            dliSa.progress =
                pokeResponse.stats.find { it.stat.name == "special-attack" }?.baseStat!!.toFloat()
            dliSd.progress =
                pokeResponse.stats.find { it.stat.name == "special-defense" }?.baseStat!!.toFloat()
            dliSp.progress =
                pokeResponse.stats.find { it.stat.name == "speed" }?.baseStat!!.toFloat()
        }
        with(Glide.with(this@PokeDetailActivity)){
            load(pokeResponse.sprites.frontDefault).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.spriteFront)
            load(pokeResponse.sprites.frontShiny).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.spriteFrontShiny)
        }
    }

    private fun getTypeColor(type: String): Int {
        return when (type) {
            "fighting" -> R.color.fighting
            "flying" -> R.color.flying
            "poison" -> R.color.poison
            "ground" -> R.color.ground
            "rock" -> R.color.rock
            "bug" -> R.color.bug
            "ghost" -> R.color.ghost
            "steel" -> R.color.steel
            "fire" -> R.color.fire
            "water" -> R.color.water
            "grass" -> R.color.grass
            "electric" -> R.color.electric
            "psychic" -> R.color.psychic
            "ice" -> R.color.ice
            "dragon" -> R.color.dragon
            "fairy" -> R.color.fairy
            "dark" -> R.color.dark
            else -> R.color.gray_21
        }
    }
}
