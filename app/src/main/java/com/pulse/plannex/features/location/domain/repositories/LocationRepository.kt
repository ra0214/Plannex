package com.pulse.plannex.features.location.domain.repositories

import com.pulse.plannex.features.location.domain.entities.LocationStatus
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getLocationUpdates(): Flow<LocationStatus>
    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float
}
