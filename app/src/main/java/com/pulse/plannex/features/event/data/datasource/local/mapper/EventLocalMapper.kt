package com.pulse.plannex.features.event.data.datasource.local.mapper

import com.pulse.plannex.features.event.data.datasource.local.entity.EventEntity
import com.pulse.plannex.features.event.domain.entities.Evento

fun EventEntity.toDomain(): Evento {
    return Evento(
        id = this.id,
        nombre = this.title,
        fecha = this.date,
        latitud = this.latitude,
        longitud = this.longitude
    )
}

fun Evento.toEntity(): EventEntity {
    return EventEntity(
        id = this.id ?: 0,
        title = this.nombre,
        description = "",
        date = this.fecha,
        latitude = this.latitud,
        longitude = this.longitud,
        qrCodeData = null,
        createdBy = 1
    )
}
