package com.pulse.plannex.features.notification.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pulse.plannex.core.network.UserDto
import com.pulse.plannex.features.notification.domain.usecases.GetUsersUseCase
import com.pulse.plannex.features.notification.domain.usecases.InvitarUsuario_UseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class InvitationUiState(
    val users: List<UserDto> = emptyList(),
    val isLoading: Boolean = false,
    val invitationResult: Result<Unit>? = null,
    val error: String? = null
)

@HiltViewModel
class InvitationViewModel @Inject constructor(
    private val invitarUsuarioUseCase: InvitarUsuario_UseCase,
    private val getUsersUseCase: GetUsersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(InvitationUiState())
    val uiState: StateFlow<InvitationUiState> = _uiState.asStateFlow()

    init {
        loadUsers()
    }

    fun loadUsers() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getUsersUseCase().onSuccess { users ->
                _uiState.update { it.copy(users = users, isLoading = false) }
            }.onFailure { e ->
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun inviteUser(eventoId: Int, userId: Int) {
        viewModelScope.launch {
            val result = invitarUsuarioUseCase(eventoId, userId)
            _uiState.update { it.copy(invitationResult = result) }
        }
    }
    
    fun resetState() {
        _uiState.update { it.copy(invitationResult = null, error = null) }
    }
}
