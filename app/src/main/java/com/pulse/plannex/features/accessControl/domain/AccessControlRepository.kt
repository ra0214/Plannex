package com.pulse.plannex.features.accessControl.domain

interface AccessControlRepository {
    suspend fun validateTicket(qrCode: String): Result<Boolean>
}
