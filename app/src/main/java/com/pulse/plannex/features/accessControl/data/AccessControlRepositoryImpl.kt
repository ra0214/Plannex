package com.pulse.plannex.features.accessControl.data

import com.pulse.plannex.core.network.EventApi
import com.pulse.plannex.features.accessControl.domain.AccessControlRepository

class AccessControlRepositoryImpl(private val api: EventApi) : AccessControlRepository {
    override suspend fun validateTicket(qrCode: String): Result<Boolean> {
        return try {
            // Simulación de validación. En un caso real, haríamos una llamada al API.
            // Por ahora, si el QR contiene la palabra "VALID", lo aceptamos.
            if (qrCode.contains("VALID", ignoreCase = true)) {
                Result.success(true)
            } else {
                Result.success(false)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
