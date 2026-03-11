package com.pulse.plannex.features.auth.domain

import androidx.fragment.app.FragmentActivity

interface BiometricAuthenticator {
    fun isBiometricAvailable(): Boolean
    fun authenticate(
        activity: FragmentActivity,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
        onFailed: () -> Unit
    )
}
