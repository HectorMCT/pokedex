package org.hector.pokedex.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import org.hector.pokedex.databinding.ItemPokemonBinding
import org.hector.pokedex.model.Pokemon

class PokeCardViewHolder(
    private val cardViewHolder: ItemPokemonBinding,
    private val clickListener: PokeClickListener
) : RecyclerView.ViewHolder(cardViewHolder.root){
    //private val sprite = view.findViewById<ImageView>(R.id.sprite_pokemon)
    //private val name = view.findViewById<TextView>(R.id.nombre_pokemon)

    fun bind(pokemon: Pokemon, context: Context){
        super.itemView
        cardViewHolder.nombrePokemon.text = pokemon.name
        Glide.with(context)
            .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"+pokemon.id+".png")
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(cardViewHolder.spritePokemon)
        /*Glide.with(context)
            .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"+pokemon.id+".png")
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(sprite)*/
        cardViewHolder.moreBtn.setOnClickListener {
            clickListener.onClick(pokemon)
        }
        cardViewHolder.card.setOnClickListener {
            clickListener.onClick(pokemon)
        }
    }
}