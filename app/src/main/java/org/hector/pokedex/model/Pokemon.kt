package org.hector.pokedex.model

import com.google.gson.annotations.SerializedName

data class Pokemon (
    var id: Int,
    var name : String = "",
    val url: String? = "",
)