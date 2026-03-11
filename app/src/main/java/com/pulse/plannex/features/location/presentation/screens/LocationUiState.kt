package com.pulse.plannex.features.location.presentation.screens

import com.pulse.plannex.features.location.domain.entities.LocationStatus

data class LocationUiState(
    val locationStatus: LocationStatus? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val eventName: String = "",
    val eventLat: Double = 0.0,
    val eventLng: Double = 0.0
)