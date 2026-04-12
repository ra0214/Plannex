package com.pulse.plannex.features.notification.domain.usecases

import com.pulse.plannex.features.notification.domain.repositories.FCMTokenRepository
import javax.inject.Inject

class RegistroTokenFCM_UseCase @Inject constructor(
    private val repository: FCMTokenRepository
) {
    suspend operator fun invoke(userId: Int, token: String): Result<Unit> {
        repository.saveToken(token)
        return repository.registerTokenOnServer(userId, token)
    }
}
