package org.hector.pokedex.data.model

import com.google.gson.annotations.SerializedName

data class PokemonDetail (
    val id: Int,
    val name : String? = "",
    val baseExperience: Int,
    val height: Int,
    val isDefault: Boolean,
    val order: Int,
    val weight: Int,
    val stats: List<PokemonStat>,
    //val moves: List<PokemonMove>,
    val types: List<PokemonType>,
    val sprites : Sprites,
)

data class Sprites(
    @SerializedName("front_default")
    val frontDefault: String? = "",
    @SerializedName("front_shiny")
    val frontShiny: String? = ""
)

data class PokemonStat(
    val stat: NamedApiResource,
    @SerializedName("effort")
    val effort: Int,
    @SerializedName("base_stat")
    val baseStat: Int
)


data class PokemonType(
    @SerializedName("slot")
    val slot: Int,
    val type: NamedApiResource
)

data class PokemonMove(
    val move: NamedApiResource,
    @SerializedName("version_group_details")
    val versionGroupDetails: List<PokemonMoveVersion>
)

data class PokemonMoveVersion(
    val moveLearnMethod: NamedApiResource,
    val versionGroup: NamedApiResource,
    @SerializedName("level_learned_at")
    val levelLearnedAt: Int
)
