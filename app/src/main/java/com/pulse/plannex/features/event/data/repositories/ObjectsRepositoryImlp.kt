package com.pulse.plannex.features.event.data.repositories

import com.pulse.plannex.core.network.EventApi
import com.pulse.plannex.core.network.EventoDto
import com.pulse.plannex.features.event.data.datasource.remote.mapper.toDomain
import com.pulse.plannex.features.event.domain.entities.Evento
import com.pulse.plannex.features.event.domain.repositories.ObjectRepository

class ObjectsRepositoryImlp(
    private val api: EventApi
) : ObjectRepository {
    override suspend fun getEventos(): List<Evento> {
        // Handle the case where the API might return a null list of events
        val eventosDto = api.getEventos().eventos
        return eventosDto?.map { it.toDomain() } ?: emptyList()
    }

    override suspend fun createEvento(nombre: String, fecha: String): Evento {
        val dto = EventoDto(nombre = nombre, fecha = fecha)
        api.createEvento(dto) 
        // Return a temporary object since the API doesn't return the created one
        return Evento(id = -1, nombre = nombre, fecha = fecha)
    }

    override suspend fun getEvento(id: Int): Evento {
        return api.getEvento(id).toDomain()
    }

    override suspend fun updateEvento(id: Int, nombre: String, fecha: String) {
        val dto = EventoDto(id = id, nombre = nombre, fecha = fecha)
        api.updateEvento(id, dto)
    }

    override suspend fun deleteEvento(id: Int) {
        api.deleteEvento(id)
    }
}
