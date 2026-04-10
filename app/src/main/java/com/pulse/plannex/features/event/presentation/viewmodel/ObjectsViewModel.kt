package com.pulse.plannex.features.event.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pulse.plannex.features.event.domain.entities.Evento
import com.pulse.plannex.features.event.domain.usecases.PostEventUseCase
import com.pulse.plannex.features.event.domain.repositories.SyncPrefsRepository
import com.pulse.plannex.features.event.presentation.screens.ObjectsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ObjectsViewModel @Inject constructor(
    private val useCase: PostEventUseCase,
    private val syncPrefs: SyncPrefsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ObjectsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            useCase.getEventosFlow().collect { localEvents ->
                _uiState.update { it.copy(upcomingEventos = localEvents) }
            }
        }
        checkSyncRequirement()
        loadEventos()
    }

    private fun checkSyncRequirement() {
        if (!syncPrefs.isSyncDoneToday()) {
            val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            if (hour >= 5) {
                _uiState.update { it.copy(showBackupDialog = true) }
            }
        }
    }

    fun dismissBackupDialog() {
        _uiState.update { it.copy(showBackupDialog = false) }
    }

    fun performManualBackup() {
        dismissBackupDialog()
        loadEventos()
        syncPrefs.saveSyncDate(System.currentTimeMillis())
    }

    fun loadEventos() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val result = useCase.refreshEventos()
            result.onSuccess { allEvents ->
                _uiState.update { it.copy(isLoading = false, eventos = allEvents, error = null) }
                syncPrefs.saveSyncDate(System.currentTimeMillis())
            }.onFailure {
                _uiState.update { it.copy(isLoading = false) }
                if (_uiState.value.eventos.isEmpty() && _uiState.value.upcomingEventos.isEmpty()) {
                    _uiState.update { it.copy(error = "Sin conexión y sin datos locales") }
                }
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
        val lat = _uiState.value.eventLat.toDoubleOrNull()
        val lng = _uiState.value.eventLng.toDoubleOrNull()

        if (name.isBlank() || date.isBlank()) {
            _uiState.update { it.copy(error = "Campos obligatorios") }
            return
        }

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val isEditing = _uiState.value.editingEventId != null
            val result = if (isEditing) {
                useCase.updateEvento(_uiState.value.editingEventId!!, name, date, lat, lng)
            } else {
                useCase.createEvento(name, date, lat, lng)
            }
            result.onSuccess { 
                loadEventos()
                cancelEdit()
            }.onFailure { e -> 
                _uiState.update { it.copy(isLoading = false, error = e.message) } 
            }
        }
    }

    fun deleteEvento(id: Int) {
        viewModelScope.launch {
            useCase.deleteEvento(id)
            loadEventos()
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
