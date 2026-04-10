package com.pulse.plannex.features.accessControl.data.datasource.remote.mapper

import com.pulse.plannex.core.network.EventoDto
import com.pulse.plannex.features.event.domain.entities.Evento

fun EventoDto.toDomain(): Evento {
    return Evento(
        id = this.id,
        nombre = this.nombre ?: this.title ?: "Evento QR",
        fecha = this.fecha ?: this.date ?: "",
        latitud = this.latitud ?: this.latitude,
        longitud = this.longitud ?: this.longitude
    )
}
