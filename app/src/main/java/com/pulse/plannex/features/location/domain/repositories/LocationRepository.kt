package com.pulse.plannex.features.location.domain.repositories

import com.pulse.plannex.features.location.domain.entities.LocationObject
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getLocationUpdates(): Flow<LocationObject>
    fun getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float
}