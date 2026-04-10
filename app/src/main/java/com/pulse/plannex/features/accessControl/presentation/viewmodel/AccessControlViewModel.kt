package com.pulse.plannex.features.accessControl.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pulse.plannex.features.accessControl.domain.repositories.HapticFeedbackManager
import com.pulse.plannex.features.accessControl.domain.usecases.RegisterEventFromQrUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccessControlViewModel @Inject constructor(
    private val registerEventFromQrUseCase: RegisterEventFromQrUseCase,
    private val hapticFeedbackManager: HapticFeedbackManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccessControlUiState())
    val uiState: StateFlow<AccessControlUiState> = _uiState.asStateFlow()

    private val _eventsUpdateChannel = MutableSharedFlow<Unit>()
    val eventsUpdateChannel = _eventsUpdateChannel.asSharedFlow()

    fun onQrCodeScanned(code: String) {
        if (_uiState.value.isProcessing || code == _uiState.value.lastScannedCode) return

        _uiState.update { it.copy(isProcessing = true, lastScannedCode = code, isValid = null, message = "Procesando código...") }

        viewModelScope.launch {
            val result = registerEventFromQrUseCase(code)
            result.onSuccess { success ->
                if (success) {
                    hapticFeedbackManager.successVibration()
                    _uiState.update { 
                        it.copy(isProcessing = false, isValid = true, message = "¡Evento Registrado con Éxito!") 
                    }
                    _eventsUpdateChannel.emit(Unit)
                } else {
                    hapticFeedbackManager.errorVibration()
                    _uiState.update { 
                        it.copy(isProcessing = false, isValid = false, message = "Código inválido o ya registrado") 
                    }
                }
            }.onFailure { error ->
                hapticFeedbackManager.errorVibration()
                _uiState.update { 
                    it.copy(isProcessing = false, isValid = false, message = "Error: ${error.localizedMessage ?: "Formato de QR incorrecto"}")
                }
            }
        }
    }

    fun resetScanner() {
        _uiState.update { AccessControlUiState() }
    }
}

data class AccessControlUiState(
    val lastScannedCode: String? = null,
    val isValid: Boolean? = null,
    val isProcessing: Boolean = false,
    val message: String? = null
)
