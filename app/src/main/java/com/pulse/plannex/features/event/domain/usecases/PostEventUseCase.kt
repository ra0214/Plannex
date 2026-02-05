package com.pulse.plannex.features.event.domain.usecases

import com.pulse.plannex.features.event.domain.entities.Evento
import com.pulse.plannex.features.event.domain.repositories.ObjectRepository

class PostEventUseCase (
    private val repository: ObjectRepository
) {
    suspend fun getEventos(): Result<List<Evento>> {
        return try {
            Result.success(repository.getEventos())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createEvento(nombre: String, fecha: String): Result<Evento> {
        return try {
            if (nombre.isBlank() || fecha.isBlank()) {
                return Result.failure(Exception("El nombre y la fecha no pueden estar vacíos"))
            }
            Result.success(repository.createEvento(nombre, fecha))
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}