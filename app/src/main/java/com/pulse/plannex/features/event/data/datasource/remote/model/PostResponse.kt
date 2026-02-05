package com.pulse.plannex.features.event.data.datasource.remote.model

import com.google.gson.annotations.SerializedName

data class PostDto(
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("fecha")
    val fecha: String,
    )
