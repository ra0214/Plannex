package com.pulse.plannex.features.event.domain.usecases

import com.pulse.plannex.features.event.domain.entities.Evento
import com.pulse.plannex.features.event.domain.repositories.ObjectRepository

class PostEventUseCase(private val repository: ObjectRepository) {
    suspend fun getEventos(): Result<List<Evento>> = runCatching {
        repository.getEventos()
    }

    suspend fun createEvento(nombre: String, fecha: String, latitud: Double? = null, longitud: Double? = null): Result<Evento> = runCatching {
        repository.createEvento(nombre, fecha, latitud, longitud)
    }

    suspend fun updateEvento(id: Int, nombre: String, fecha: String, latitud: Double? = null, longitud: Double? = null): Result<Unit> = runCatching {
        repository.updateEvento(id, nombre, fecha, latitud, longitud)
    }

    suspend fun deleteEvento(id: Int): Result<Unit> = runCatching {
        repository.deleteEvento(id)
    }
}