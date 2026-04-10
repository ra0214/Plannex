package com.pulse.plannex.features.event.presentation.screens

import com.pulse.plannex.features.event.domain.entities.Evento

data class ObjectsUiState (
    val isLoading: Boolean = false,
    val eventos: List<Evento> = emptyList(),
    val upcomingEventos: List<Evento> = emptyList(),
    val error: String? = null,
    val isRefreshing: Boolean = false,
    val eventName: String = "",
    val eventDate: String = "",
    val eventLat: String = "",
    val eventLng: String = "",
    val editingEventId: Int? = null,
    val showBackupDialog: Boolean = false
)
