package com.pulse.plannex.features.accessControl.data.repositories

import com.google.gson.Gson
import com.pulse.plannex.core.network.EventApi
import com.pulse.plannex.core.network.EventoDto
import com.pulse.plannex.features.accessControl.domain.repositories.AccessControlRepository
import javax.inject.Inject

class AccessControlRepositoryImpl @Inject constructor(private val api: EventApi) : AccessControlRepository {
    private val gson = Gson()

    override suspend fun registerEventFromQr(qrContent: String): Result<Boolean> {
        return try {
            val rawDto = gson.fromJson(qrContent, EventoDto::class.java)
            
            val normalizedDto = rawDto.copy(
                title = rawDto.title ?: rawDto.nombre,
                date = rawDto.date ?: rawDto.fecha,
                latitude = rawDto.latitude ?: rawDto.latitud,
                longitude = rawDto.longitude ?: rawDto.longitud,
                qrCodeData = rawDto.qrCodeData ?: "QR-GENERATED",
                createdBy = rawDto.createdBy ?: 1
            )

            if (normalizedDto.title.isNullOrBlank() || normalizedDto.date.isNullOrBlank()) {
                return Result.failure(Exception("Datos incompletos en el QR"))
            }

            val response = api.createEvento(normalizedDto)
            
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                val errorBody = response.errorBody()?.string() ?: response.message()
                Result.failure(Exception("Error servidor: $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
