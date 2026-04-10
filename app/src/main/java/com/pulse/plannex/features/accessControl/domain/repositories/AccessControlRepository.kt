package com.pulse.plannex.features.accessControl.domain.repositories

interface AccessControlRepository {
    suspend fun registerEventFromQr(qrContent: String): Result<Boolean>
}
