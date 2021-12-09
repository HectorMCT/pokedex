package org.hector.pokedex.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.hector.pokedex.data.model.Pokemon

import org.hector.pokedex.databinding.ItemPokemonBinding

class PokeListAdapter (
    var context: Context,
    private val pokemons : MutableList<Pokemon>,
    private val clickListener: PokeClickListener
    ) : RecyclerView.Adapter<PokeCardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokeCardViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding =  ItemPokemonBinding.inflate(from, parent, false)
        return PokeCardViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: PokeCardViewHolder, position: Int) {
        val pokemon = pokemons[position]
        Log.e("POKEMON","Pokemon ${pokemon.id}")
        holder.bind(pokemon, context)
    }

    override fun getItemCount(): Int {
        return pokemons.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addListPokemon(results: MutableList<Pokemon>?) {
        if (results != null) {
            pokemons.addAll(results)
            notifyDataSetChanged()
        }
    }
}