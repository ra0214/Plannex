package com.pulse.plannex.features.event.domain.entities

data class Evento(
    val id: Int? = null,
    val nombre: String,
    val fecha: String,
    val latitud: Double? = null,
    val longitud: Double? = null
)