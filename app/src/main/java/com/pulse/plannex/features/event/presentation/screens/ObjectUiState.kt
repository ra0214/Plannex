package com.pulse.plannex.features.event.presentation.screens

import com.pulse.plannex.features.event.domain.entities.Object

data class ObjectsUiState (
    val isLoading: Boolean = false,
    val objects: List<Object> = emptyList(),
    val error: String? = null,
    val isRefreshing: Boolean = false
)