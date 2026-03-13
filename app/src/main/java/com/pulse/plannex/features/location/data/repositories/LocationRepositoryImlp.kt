package com.pulse.plannex.features.location.data.repositories

import com.pulse.plannex.features.location.domain.entities.LocationObject
import com.pulse.plannex.features.location.domain.repositories.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

// Este archivo es un duplicado con error de nombre. 
// La implementación real está en LocationRepositoryImpl.kt
class LocationRepositoryImlp : LocationRepository {
    override fun getLocationUpdates(): Flow<LocationObject> = emptyFlow()
    override fun getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float = 0f
}