package com.pulse.plannex.features.event.domain.usecases

import com.pulse.plannex.features.event.domain.entities.Evento
import com.pulse.plannex.features.event.domain.repositories.ObjectRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostEventUseCase @Inject constructor(
    private val repository: ObjectRepository
) {
    fun getEventosFlow(): Flow<List<Evento>> = repository.getEventosFlow()
    
    fun getUpcomingEventosFlow(): Flow<List<Evento>> = repository.getUpcomingEventosFlow()

    suspend fun refreshEventos(): Result<List<Evento>> = repository.refreshEventos()

    suspend fun createEvento(nombre: String, fecha: String, latitud: Double? = null, longitud: Double? = null): Result<Evento> = 
        repository.createEvento(nombre, fecha, latitud, longitud)

    suspend fun updateEvento(id: Int, nombre: String, fecha: String, latitud: Double? = null, longitud: Double? = null): Result<Unit> = 
        repository.updateEvento(id, nombre, fecha, latitud, longitud)

    suspend fun deleteEvento(id: Int): Result<Unit> = repository.deleteEvento(id)
}
