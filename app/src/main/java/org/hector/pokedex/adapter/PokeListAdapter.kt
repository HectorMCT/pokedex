package org.hector.pokedex.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.hector.pokedex.model.Pokemon

import org.hector.pokedex.databinding.ItemPokemonBinding

class PokeListAdapter (
    var context: Context,
    private val pokemons : MutableList<Pokemon>,
    private val clickListener: PokeClickListener
    ) : RecyclerView.Adapter<PokeCardViewHolder>() {

    /*class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        private val sprite = view.findViewById<ImageView>(R.id.sprite_pokemon)
        private val name = view.findViewById<TextView>(R.id.nombre_pokemon)

        fun bind(pokemon: Pokemon, context: Context){
            super.itemView
            name.text = pokemon.name
            Glide.with(context)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"+pokemon.id+".png")
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(sprite)
        }
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokeCardViewHolder {
        /*val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_pokemon, parent, false)*/
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