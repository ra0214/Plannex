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

    fun submitEvento() {
        val name = _uiState.value.eventName
        val date = _uiState.value.eventDate

        if (name.isBlank() || date.isBlank()) {
            _uiState.update { it.copy(error = "El nombre y la fecha son obligatorios.") }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val isEditing = _uiState.value.editingEventId != null
            val result = if (isEditing) {
                useCase.updateEvento(_uiState.value.editingEventId!!, name, date)
            } else {
                useCase.createEvento(name, date)
            }

            result.fold(
                onSuccess = {
                    cancelEdit() // Limpia el formulario y sale del modo edición
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
                eventDate = evento.fecha
            )
        }
    }

    fun cancelEdit() {
        _uiState.update {
            it.copy(
                editingEventId = null,
                eventName = "",
                eventDate = ""
            )
        }
    }
}