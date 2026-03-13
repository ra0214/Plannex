package com.pulse.plannex.features.accessControl.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pulse.plannex.features.accessControl.domain.AccessControlRepository
import com.pulse.plannex.features.accessControl.domain.HapticFeedbackManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AccessControlUiState(
    val lastScannedCode: String? = null,
    val isValid: Boolean? = null,
    val isProcessing: Boolean = false,
    val message: String? = null
)

class AccessControlViewModel(
    private val repository: AccessControlRepository,
    private val hapticFeedbackManager: HapticFeedbackManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccessControlUiState())
    val uiState: StateFlow<AccessControlUiState> = _uiState.asStateFlow()

    fun onQrCodeScanned(code: String) {
        if (_uiState.value.isProcessing || code == _uiState.value.lastScannedCode) return

        _uiState.update { it.copy(isProcessing = true, lastScannedCode = code, isValid = null) }

        viewModelScope.launch {
            val result = repository.validateTicket(code)
            result.onSuccess { isValid ->
                if (isValid) {
                    hapticFeedbackManager.successVibration()
                    _uiState.update { 
                        it.copy(isProcessing = false, isValid = true, message = "Ticket Válido: $code") 
                    }
                } else {
                    hapticFeedbackManager.errorVibration()
                    _uiState.update { 
                        it.copy(isProcessing = false, isValid = false, message = "Ticket Inválido") 
                    }
                }
            }.onFailure {
                hapticFeedbackManager.errorVibration()
                _uiState.update { 
                    it.copy(isProcessing = false, isValid = false, message = "Error al validar") 
                }
            }
        }
    }

    fun resetScanner() {
        _uiState.update { AccessControlUiState() }
    }
}
