package com.pulse.plannex.features.location.domain.usecases

import com.pulse.plannex.features.location.domain.entities.LocationStatus
import com.pulse.plannex.features.location.domain.repositories.LocationRepository
import kotlinx.coroutines.flow.Flow

class GetLocationUpdatesUseCase(private val repository: LocationRepository) {
    operator fun invoke(): Flow<LocationStatus> = repository.getLocationUpdates()
    
    fun calculateDistance(startLat: Double, startLng: Double, endLat: Double, endLng: Double): Float {
        return repository.calculateDistance(startLat, startLng, endLat, endLng)
    }
}
