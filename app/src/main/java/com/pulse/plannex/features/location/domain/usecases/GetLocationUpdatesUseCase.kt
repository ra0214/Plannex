package com.pulse.plannex.features.location.domain.usecases

import com.pulse.plannex.features.location.domain.entities.LocationObject
import com.pulse.plannex.features.location.domain.repositories.LocationRepository
import kotlinx.coroutines.flow.Flow

class GetLocationUpdatesUseCase(private val repository: LocationRepository) {
    operator fun invoke(): Flow<LocationObject> = repository.getLocationUpdates()
    
    fun calculateDistance(startLat: Double, startLng: Double, endLat: Double, endLng: Double): Float {
        return repository.getDistance(startLat, startLng, endLat, endLng)
    }
}