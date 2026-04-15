package com.pulse.plannex.features.notification.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.pulse.plannex.features.notification.domain.usecases.RegistroTokenFCM_UseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FCMViewModel @Inject constructor(
    private val registroTokenUseCase: RegistroTokenFCM_UseCase,
    private val firebaseMessaging: FirebaseMessaging
) : ViewModel() {

    private val _registrationState = MutableStateFlow<Result<Unit>?>(null)
    val registrationState: StateFlow<Result<Unit>?> = _registrationState

    fun registerToken(userId: Int) {
        firebaseMessaging.token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d("FCM_TOKEN", "Token de mi dispositivo: $token")
                
                viewModelScope.launch {
                    _registrationState.value = registroTokenUseCase(userId, token)
                }
            } else {
                Log.e("FCM_TOKEN", "Error al obtener el token", task.exception)
                _registrationState.value = Result.failure(task.exception ?: Exception("FCM token retrieval failed"))
            }
        }
    }
}
