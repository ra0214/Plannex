package com.pulse.plannex.features.event.domain.repositories

import com.pulse.plannex.features.event.domain.entities.Evento
import kotlinx.coroutines.flow.Flow

interface ObjectRepository {
    fun getEventosFlow(): Flow<List<Evento>>
    fun getUpcomingEventosFlow(): Flow<List<Evento>>
    suspend fun refreshEventos(): Result<List<Evento>>
    
    suspend fun createEvento(nombre: String, fecha: String, latitud: Double? = null, longitud: Double? = null): Result<Evento>
    suspend fun getEvento(id: Int): Evento
    suspend fun updateEvento(id: Int, nombre: String, fecha: String, latitud: Double? = null, longitud: Double? = null): Result<Unit>
    suspend fun deleteEvento(id: Int): Result<Unit>
}
