package com.pulse.plannex.features.event.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pulse.plannex.features.event.domain.usecases.PostEventUseCase
import com.pulse.plannex.features.event.presentation.screens.ObjectsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ObjectsViewModel (
    private val useCase: PostEventUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ObjectsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadEventos()
    }

    private fun loadEventos() {
        _uiState.update { it.copy(isLoading = true) }
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

    fun createNewEvento(nombre: String, fecha: String) {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val result = useCase.createEvento(nombre, fecha)
            _uiState.update { currentState ->
                result.fold(
                    onSuccess = { newEvento ->
                        currentState.copy(
                            isLoading = false,
                            eventos = listOf(newEvento) + currentState.eventos
                        )
                    },
                    onFailure = { error ->
                        currentState.copy(isLoading = false, error = error.message)
                    }
                )
            }
        }
    }
}