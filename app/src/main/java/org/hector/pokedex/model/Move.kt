package org.hector.pokedex.model

data class Move(
    val id: Int,
    val names: List<Name>
)

data class Name(
    val name: String,
    val language: NamedApiResource
)