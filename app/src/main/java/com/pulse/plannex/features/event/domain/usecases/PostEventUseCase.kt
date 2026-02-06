package com.pulse.plannex.features.event.domain.usecases

import com.pulse.plannex.features.event.domain.entities.Evento
import com.pulse.plannex.features.event.domain.repositories.ObjectRepository

class PostEventUseCase(private val repository: ObjectRepository) {
    suspend fun getEventos(): Result<List<Evento>> = runCatching {
        repository.getEventos()
    }

    suspend fun createEvento(nombre: String, fecha: String): Result<Evento> = runCatching {
        repository.createEvento(nombre, fecha)
    }

    suspend fun updateEvento(id: Int, nombre: String, fecha: String): Result<Unit> = runCatching {
        repository.updateEvento(id, nombre, fecha)
    }

    suspend fun deleteEvento(id: Int): Result<Unit> = runCatching {
        repository.deleteEvento(id)
    }
}