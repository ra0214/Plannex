package com.pulse.plannex.features.notification.domain.usecases

import com.pulse.plannex.features.notification.domain.repositories.FCMTokenRepository
import javax.inject.Inject

class EliminarTokenFCM_UseCase @Inject constructor(
    private val repository: FCMTokenRepository
) {
    suspend operator fun invoke(userId: Int): Result<Unit> {
        return repository.deleteTokenFromServer(userId)
    }
}
