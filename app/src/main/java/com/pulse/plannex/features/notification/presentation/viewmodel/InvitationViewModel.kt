package com.pulse.plannex.features.notification.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pulse.plannex.features.notification.domain.usecases.InvitarUsuario_UseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvitationViewModel @Inject constructor(
    private val invitarUsuarioUseCase: InvitarUsuario_UseCase
) : ViewModel() {

    private val _invitationState = MutableStateFlow<Result<Unit>?>(null)
    val invitationState: StateFlow<Result<Unit>?> = _invitationState

    fun inviteUser(eventoId: Int, userId: Int) {
        viewModelScope.launch {
            _invitationState.value = invitarUsuarioUseCase(eventoId, userId)
        }
    }
    
    fun resetState() {
        _invitationState.value = null
    }
}
