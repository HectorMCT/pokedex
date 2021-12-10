package org.hector.pokedex.ui.main.adapter

import org.hector.pokedex.data.model.Pokemon

interface PokeClickListener {
    fun onClick(poke: Pokemon)
}