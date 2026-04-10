package com.pulse.plannex.features.accessControl.domain.usecases

import com.pulse.plannex.features.accessControl.domain.repositories.AccessControlRepository
import javax.inject.Inject

class RegisterEventFromQrUseCase @Inject constructor(
    private val repository: AccessControlRepository
) {
    suspend operator fun invoke(qrContent: String): Result<Boolean> {
        return repository.registerEventFromQr(qrContent)
    }
}
