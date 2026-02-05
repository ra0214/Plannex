package com.pulse.plannex.features.event.data.datasource.remote.mapper

fun PostDto.toDomain(): Object{
    return Object(
        nombre = this.nombre,
        fecha = this.fecha,
}