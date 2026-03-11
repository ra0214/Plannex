package com.pulse.plannex.features.location.domain.entities

data class LocationStatus(
    val latitude: Double,
    val longitude: Double,
    val distanceToEvent: Float? = null,
    val hasArrived: Boolean = false
)