package com.pulse.plannex.features.location.data.datasource.remote.mapper

import com.pulse.plannex.features.location.data.datasource.remote.model.LocationResponse
import com.pulse.plannex.features.location.domain.entities.LocationObject

fun LocationResponse.toDomain(): LocationObject {
    return LocationObject(
        latitude = this.latitude,
        longitude = this.longitude
    )
}