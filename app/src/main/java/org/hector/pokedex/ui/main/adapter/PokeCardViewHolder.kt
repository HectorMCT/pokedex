package org.hector.pokedex.ui.main.adapter

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

    fun bind(pokemon: Pokemon, context: Context){
        super.itemView
        cardViewHolder.nombrePokemon.text = pokemon.name
        Glide.with(context)
            .load(ApiClient.SPRITE_URL+pokemon.id+".png")
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(cardViewHolder.spritePokemon)

        cardViewHolder.moreBtn.setOnClickListener {
            clickListener.onClick(pokemon)
        }
        cardViewHolder.card.setOnClickListener {
            clickListener.onClick(pokemon)
        }
    }
}