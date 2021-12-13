package org.hector.pokedex.adapter

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import org.hector.pokedex.databinding.ItemPokemonBinding
import org.hector.pokedex.data.model.Pokemon
import org.hector.pokedex.data.api.ApiClient

class PokeCardViewHolder(
    private val cardViewHolder: ItemPokemonBinding,
    private val clickListener: PokeClickListener
) : RecyclerView.ViewHolder(cardViewHolder.root){

    @SuppressLint("SetTextI18n")
    fun bind(pokemon: Pokemon, context: Context){
        super.itemView
        cardViewHolder.idPokemon.text = "#" + pokemon.id.toString()
        cardViewHolder.nombrePokemon.text = pokemon.name
        Glide.with(context)
            .load(ApiClient.SPRITE_URL+pokemon.name.lowercase()+".gif")
            .centerCrop()
            .centerInside()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(cardViewHolder.spritePokemon)

        cardViewHolder.card.setOnClickListener {
            clickListener.onClick(pokemon)
        }
    }
}