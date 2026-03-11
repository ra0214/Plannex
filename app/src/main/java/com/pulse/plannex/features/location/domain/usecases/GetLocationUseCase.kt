package com.pulse.plannex.features.location.domain.usecases

import com.pulse.plannex.features.location.domain.entities.LocationObject
import com.pulse.plannex.features.location.domain.repositories.LocationRepository
import kotlinx.coroutines.flow.Flow

class GetLocationUseCase(private val repository: LocationRepository) {
    
    operator fun invoke(): Flow<LocationObject> = repository.getLocationUpdates()
    
    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        return repository.getDistance(lat1, lon1, lat2, lon2)
    }
}