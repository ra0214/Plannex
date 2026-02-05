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
        return api.getEventos().map { it.toDomain() }
    }

    override suspend fun createEvento(nombre: String, fecha: String): Evento {
        val dto = EventoDto(nombre = nombre, fecha = fecha)
        return api.createEvento(dto).toDomain()
    }

    override suspend fun getEvento(id: Int): Evento {
        return api.getEvento(id).toDomain()
    }

    override suspend fun updateEvento(id: Int, nombre: String, fecha: String): Evento {
        val dto = EventoDto(id = id, nombre = nombre, fecha = fecha)
        return api.updateEvento(id, dto).toDomain()
    }

    override suspend fun deleteEvento(id: Int) {
        api.deleteEvento(id)
    }
}