package com.pulse.plannex.features.event.data.datasource.remote.mapper

import com.pulse.plannex.core.network.EventoDto
import com.pulse.plannex.features.event.domain.entities.Evento

fun EventoDto.toDomain(): Evento {
    return Evento(
        id = this.id,
        nombre = this.title,
        fecha = this.date,
        latitud = this.latitude,
        longitud = this.longitude
    )
}

fun Evento.toDto(): EventoDto {
    return EventoDto(
        id = this.id,
        title = this.nombre,
        date = this.fecha,
        latitude = this.latitud,
        longitude = this.longitud
    )
}
