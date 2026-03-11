package com.pulse.plannex.features.event.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pulse.plannex.features.event.domain.entities.Evento
import com.pulse.plannex.features.event.domain.usecases.PostEventUseCase
import com.pulse.plannex.features.event.presentation.screens.ObjectsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ObjectsViewModel(
    private val useCase: PostEventUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ObjectsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadEventos()
    }

    fun loadEventos() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val result = useCase.getEventos()
            _uiState.update { currentState ->
                result.fold(
                    onSuccess = { list ->
                        currentState.copy(isLoading = false, eventos = list)
                    },
                    onFailure = { error ->
                        currentState.copy(isLoading = false, error = error.message)
                    }
                )
            }
        }
    }

    fun onEventNameChanged(name: String) {
        _uiState.update { it.copy(eventName = name, error = null) }
    }

    fun onEventDateChanged(date: String) {
        _uiState.update { it.copy(eventDate = date, error = null) }
    }

    fun onEventLocationChanged(lat: String, lng: String) {
        _uiState.update { it.copy(eventLat = lat, eventLng = lng) }
    }

    fun submitEvento() {
        val name = _uiState.value.eventName
        val date = _uiState.value.eventDate
        val latStr = _uiState.value.eventLat
        val lngStr = _uiState.value.eventLng

        if (name.isBlank() || date.isBlank()) {
            _uiState.update { it.copy(error = "El nombre y la fecha son obligatorios.") }
            return
        }

        val lat = latStr.toDoubleOrNull()
        val lng = lngStr.toDoubleOrNull()

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val isEditing = _uiState.value.editingEventId != null
            val result = if (isEditing) {
                useCase.updateEvento(_uiState.value.editingEventId!!, name, date, lat, lng)
            } else {
                useCase.createEvento(name, date, lat, lng)
            }

            result.fold(
                onSuccess = {
                    cancelEdit() 
                    loadEventos()
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
            )
        }
    }

    fun deleteEvento(id: Int) {
        viewModelScope.launch {
            val result = useCase.deleteEvento(id)
            if (result.isSuccess) {
                loadEventos()
            } else {
                _uiState.update { it.copy(error = result.exceptionOrNull()?.message) }
            }
        }
    }

    fun startEdit(evento: Evento) {
        _uiState.update {
            it.copy(
                editingEventId = evento.id,
                eventName = evento.nombre,
                eventDate = evento.fecha,
                eventLat = evento.latitud?.toString() ?: "",
                eventLng = evento.longitud?.toString() ?: ""
            )
        }
    }

    fun cancelEdit() {
        _uiState.update {
            it.copy(
                editingEventId = null,
                eventName = "",
                eventDate = "",
                eventLat = "",
                eventLng = ""
            )
        }
    }
}
