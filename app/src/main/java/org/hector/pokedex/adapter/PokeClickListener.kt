package org.hector.pokedex.adapter

import org.hector.pokedex.data.model.Pokemon

interface PokeClickListener {
    fun onClick(poke: Pokemon)
}