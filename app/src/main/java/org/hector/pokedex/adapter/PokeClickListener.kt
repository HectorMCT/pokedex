package org.hector.pokedex.adapter

import org.hector.pokedex.model.Pokemon

interface PokeClickListener {
    fun onClick(poke: Pokemon)
}