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
        val response = api.getEventos(createdBy = 1)
        return if (response.isSuccessful) {
            response.body()?.eventos?.map { it.toDomain() } ?: emptyList()
        } else {
            emptyList()
        }
    }

    override suspend fun createEvento(nombre: String, fecha: String, latitud: Double?, longitud: Double?): Evento {
        val dto = EventoDto(
            title = nombre,
            date = fecha,
            latitude = latitud,
            longitude = longitud,
            createdBy = 1
        )
        api.createEvento(dto)
        return Evento(id = -1, nombre = nombre, fecha = fecha, latitud = latitud, longitud = longitud)
    }

    override suspend fun getEvento(id: Int): Evento {
        return api.getEvento(id).toDomain()
    }

    override suspend fun updateEvento(id: Int, nombre: String, fecha: String, latitud: Double?, longitud: Double?) {
        val dto = EventoDto(
            id = id,
            title = nombre,
            date = fecha,
            latitude = latitud,
            longitude = longitud,
            createdBy = 1
        )
        api.updateEvento(id, dto)
    }

    override suspend fun deleteEvento(id: Int) {
        api.deleteEvento(id)
    }
}
