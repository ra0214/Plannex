package com.pulse.plannex.features.auth.presentation.viewmodel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pulse.plannex.core.network.EventApi
import com.pulse.plannex.core.network.LoginRequest
import com.pulse.plannex.features.auth.domain.BiometricAuthenticator
import com.pulse.plannex.features.auth.domain.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val userId: Int? = null,
    val error: String? = null,
    val isBiometricAvailable: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val api: EventApi,
    private val authRepository: AuthRepository,
    private val biometricAuthenticator: BiometricAuthenticator
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState(isBiometricAvailable = biometricAuthenticator.isBiometricAvailable()))
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(userName: String, password: String) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            try {
                val response = api.login(LoginRequest(userName, password))
                
                if (response.isSuccessful) {
                    val loginBody = response.body()
                    
                    // Verificamos si la respuesta indica éxito según los criterios del backend
                    val isSuccessful = loginBody?.status?.equals("success", ignoreCase = true) == true || 
                                      loginBody?.message?.contains("exitoso", ignoreCase = true) == true ||
                                      loginBody?.token != null

                    if (isSuccessful) {
                        // IMPORTANTE: Guardamos el token y el userId en el almacenamiento local
                        loginBody?.token?.let { token ->
                            authRepository.saveToken(token)
                        }
                        
                        loginBody?.userId?.let { userId ->
                            authRepository.saveUserId(userId)
                        }
                        
                        _uiState.update { it.copy(
                            isLoading = false, 
                            isSuccess = true,
                            userId = loginBody?.userId
                        ) }
                    } else {
                        _uiState.update { it.copy(isLoading = false, error = loginBody?.message ?: "Credenciales incorrectas") }
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Error del servidor"
                    _uiState.update { it.copy(isLoading = false, error = "Error ${response.code()}: $errorBody") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Error de conexión: ${e.message}") }
            }
        }
    }

    fun loginWithBiometrics(activity: FragmentActivity) {
        biometricAuthenticator.authenticate(
            activity = activity,
            onSuccess = {
                _uiState.update { it.copy(isSuccess = true) }
            },
            onError = { error ->
                _uiState.update { it.copy(error = error) }
            },
            onFailed = {
                _uiState.update { it.copy(error = "Autenticación fallida") }
            }
        )
    }
}
