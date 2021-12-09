package org.hector.pokedex.data.model


val POKEMON_ID = "POKEMON_ID"

data class Pokemon (
    var id: Int,
    var name : String = "",
    val url: String? = "",
)