package org.hector.pokedex.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.hector.pokedex.adapter.PokeClickListener
import org.hector.pokedex.adapter.PokeListAdapter
import org.hector.pokedex.databinding.ActivityMainBinding
import org.hector.pokedex.data.model.POKEMON_ID
import org.hector.pokedex.data.model.PokeList
import org.hector.pokedex.data.model.Pokemon
import org.hector.pokedex.data.api.ApiClient
import org.hector.pokedex.data.api.PokeListServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


import es.dmoral.toasty.Toasty
import android.graphics.Typeface.BOLD_ITALIC
import org.hector.pokedex.R


class MainActivity : AppCompatActivity(), PokeClickListener {

    private lateinit var binding: ActivityMainBinding

    private var pokeListAdapter = PokeListAdapter(this, ArrayList(), this)
    private val layoutManage = GridLayoutManager(this,2)

    private var offset : Int = 0
    private var loading : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loading = true
        setUpUI()
        obtenerDatosPokemon()
    }

    //configuramos el RecyclerView
    private fun setUpUI(){
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = layoutManage
            adapter = pokeListAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if(dy > 0){
                        val visibleItemCount = layoutManage.childCount
                        val totalItemCount = layoutManage.itemCount
                        val pastVisibleItems = layoutManage.findFirstVisibleItemPosition()

                        if (loading) {
                            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                loading = false
                                offset += 20
                                obtenerDatosPokemon()
                            }
                        }
                    }
                }
            })
        }
    }

    private fun obtenerDatosPokemon() {

        //obteniendo la interfaz donde se define la API
        val endpoint = ApiClient.apiClient().create(PokeListServices::class.java)

        val pokeCall = endpoint.getPokemon(20, this.offset)

        pokeCall.enqueue(object : Callback<PokeList> {
            //imprimimos algo si no nos llegó respuesta
            override fun onFailure(call: Call<PokeList>, t: Throwable) {
                Toasty.custom(this@MainActivity, getString(R.string.error_conection), R.drawable.ic_clear_white_24dp, R.color.warningColor, Toast.LENGTH_LONG, true, true).show()
            }

            //mostramos los archivos solo si el resultado es 200
            override fun onResponse(call: Call<PokeList>, response: Response<PokeList>) {
                loading = true
                if(response.isSuccessful) {
                    val pokeResponse = response.body()
                    if (pokeResponse != null) {
                        pokeResponse.results?.forEach {
                            it.name = it.name.substring(0, 1).uppercase(Locale.getDefault()) + it.name.substring(1)
                            it.id = it.url?.split("/")!![6].toInt()
                        }
                        pokeListAdapter.addListPokemon(pokeResponse.results)
                    }
                } else{
                    Toasty.custom(this@MainActivity, getString(R.string.error_conection), R.drawable.ic_clear_white_24dp, R.color.warningColor, Toast.LENGTH_LONG, true, true).show()
                }
            }

        })
    }

    override fun onClick(poke: Pokemon) {
        val intent = Intent(applicationContext, PokeDetailActivity::class.java)
        intent.putExtra(POKEMON_ID, poke.id)
        startActivity(intent)
    }
}