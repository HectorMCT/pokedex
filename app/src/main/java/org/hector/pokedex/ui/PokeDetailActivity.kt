package org.hector.pokedex.ui

import android.graphics.drawable.GradientDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.getColor
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import es.dmoral.toasty.Toasty
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokedetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pokeID :Int = intent.getIntExtra(POKEMON_ID, -1)

        Toasty.custom(this@PokeDetailActivity, getString(R.string.who), R.drawable.ic_info_outline_white_24dp, R.color.infoColor, Toast.LENGTH_LONG, true, true).show()
        obtenerDatosPokemon(pokeID)
    }

    private fun obtenerDatosPokemon(pokemonID: Int) {

        val pokeCall = ApiClient.apiClient().create(PokeServices::class.java).getPokemon(pokemonID)

        pokeCall.enqueue(object : Callback<PokemonDetail> {

            override fun onFailure(call: Call<PokemonDetail>, t: Throwable) {
                Toasty.custom(this@PokeDetailActivity, getString(R.string.error_conection), R.drawable.ic_clear_white_24dp, R.color.warningColor, Toast.LENGTH_LONG, true, true).show()
            }

            override fun onResponse(call: Call<PokemonDetail>, response: Response<PokemonDetail>) {
                if(response.isSuccessful) {
                    val pokeResponse = response.body()
                    if (pokeResponse != null) {
                        pokeResponse.name =  pokeResponse.name?.substring(0, 1)?.uppercase(Locale.getDefault()) + pokeResponse.name?.substring(1)
                        setUpUI(pokeResponse)
                        Toasty.custom(this@PokeDetailActivity, getString(R.string.it_is, pokeResponse.name), R.drawable.ic_check_white_24dp, R.color.successColor, Toast.LENGTH_LONG, true, true).show()
                    }
                } else{
                    Toasty.custom(this@PokeDetailActivity, getString(R.string.error_conection), R.drawable.ic_clear_white_24dp, R.color.warningColor, Toast.LENGTH_LONG, true, true).show()
                }
            }

        })
    }

    private fun setUpUI(pokeResponse: PokemonDetail) {

        //Setting the background color related to the pokemon types
        with(binding){
            if (pokeResponse.types.size == 1) {
                val types = arrayListOf(getTypeColor(pokeResponse.types[0].type.name))
                typeOne.text = pokeResponse.types[0].type.name
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val gradientDrawable = GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        intArrayOf(getColor(types[0]), getColor(types[0]))
                    )
                    pokeLayout.background = gradientDrawable
                    typeOne.setBackgroundColor(getColor(types[0]))
                } else {
                    val gradientDrawable = GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        intArrayOf(
                            getColor(this@PokeDetailActivity,types[0]),
                            getColor(this@PokeDetailActivity,types[0]))
                    )
                    pokeLayout.background = gradientDrawable
                    typeOne.setBackgroundColor(getColor(this@PokeDetailActivity,types[0]))
                }
                typeOne.visibility = View.VISIBLE
            } else if (pokeResponse.types.size == 2) {
                typeTwo.text = pokeResponse.types[0].type.name
                typeThree.text = pokeResponse.types[1].type.name
                val types = arrayListOf(getTypeColor(pokeResponse.types[0].type.name), getTypeColor(pokeResponse.types[1].type.name))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val gradientDrawable = GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        intArrayOf(getColor(types[0]), getColor(types[1]))
                    )
                    pokeLayout.background = gradientDrawable
                    typeTwo.setBackgroundColor(getColor(types[0]))
                    typeThree.setBackgroundColor(getColor(types[1]))
                } else {
                    val gradientDrawable = GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        intArrayOf(
                            getColor(this@PokeDetailActivity,types[0]),
                            getColor(this@PokeDetailActivity,types[1])
                        )
                    )
                    pokeLayout.background = gradientDrawable
                    typeTwo.setBackgroundColor(getColor(this@PokeDetailActivity,types[0]))
                    typeThree.setBackgroundColor(getColor(this@PokeDetailActivity,types[1]))
                }
                typeTwo.visibility = View.VISIBLE
                typeThree.visibility = View.VISIBLE
            }
        }

        with(binding) {
            //Pokemon name and number
            pokeName.text = getString(R.string.poke_name, pokeResponse.order, pokeResponse.name)

            //Pokemon height and weight
            peso.text = getString(R.string.peso, (pokeResponse.weight.toDouble() / 10))
            altura.text = getString(R.string.altura, (pokeResponse.height.toDouble() / 10))

            //Pokemon base stats
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

            //pokeStats.visibility = View.VISIBLE
            //Setting progres bar from 0 to 255 related to the base stats
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
            else -> R.color.gray
        }
    }
}
