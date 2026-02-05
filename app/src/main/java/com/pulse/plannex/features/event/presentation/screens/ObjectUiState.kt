package com.pulse.plannex.features.event.presentation.screens

import com.pulse.plannex.features.event.domain.entities.Evento

data class ObjectsUiState (
    val isLoading: Boolean = false,
    val eventos: List<Evento> = emptyList(),
    val error: String? = null,
    val isRefreshing: Boolean = false
)