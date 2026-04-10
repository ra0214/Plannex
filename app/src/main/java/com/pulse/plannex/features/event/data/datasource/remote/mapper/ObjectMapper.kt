package com.pulse.plannex.features.event.data.datasource.remote.mapper

import com.pulse.plannex.core.network.EventoDto
import com.pulse.plannex.features.event.domain.entities.Evento

fun EventoDto.toDomain(): Evento {
    return Evento(
        id = this.id,
        nombre = this.nombre ?: this.title ?: "Sin nombre",
        fecha = this.fecha ?: this.date ?: "Sin fecha",
        latitud = this.latitud ?: this.latitude,
        longitud = this.longitud ?: this.longitude
    )
}

fun Evento.toDto(): EventoDto {
    return EventoDto(
        id = this.id,
        nombre = this.nombre,
        fecha = this.fecha,
        latitud = this.latitud,
        longitud = this.longitud
    )
}
