package com.pulse.plannex.features.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pulse.plannex.core.network.EventApi
import com.pulse.plannex.core.network.LoginRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

class LoginViewModel(private val api: EventApi) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(userName: String, password: String) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            try {
                val response = api.login(LoginRequest(userName, password))
                
                // Flexibilidad: Consideramos éxito si el status es success, 
                // o si el mensaje dice "exitoso", o si nos llega un token.
                val isSuccessful = response.status.equals("success", ignoreCase = true) || 
                                  response.message?.contains("exitoso", ignoreCase = true) == true ||
                                  response.token != null

                if (isSuccessful) {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = response.message ?: "Credenciales incorrectas") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Error de conexión: ${e.localizedMessage}") }
            }
        }
    }
}
