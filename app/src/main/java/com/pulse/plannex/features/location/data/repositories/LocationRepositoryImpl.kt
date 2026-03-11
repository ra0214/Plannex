package com.pulse.plannex.features.location.data.repositories

import android.location.Location
import com.pulse.plannex.features.location.domain.entities.LocationStatus
import com.pulse.plannex.features.location.domain.repositories.LocationRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LocationRepositoryImpl : LocationRepository {

    override fun getLocationUpdates(): Flow<LocationStatus> = flow {
        // Simulamos el sensor de hardware GPS
        var currentLat = -12.0463
        var currentLng = -77.0310
        
        while (true) {
            emit(LocationStatus(currentLat, currentLng))
            delay(3000) // Actualizamos cada 3 segundos
            // El usuario "se mueve" hacia el evento
            currentLat += 0.0001
            currentLng += 0.0001
        }
    }

    override fun calculateDistance(startLat: Double, startLng: Double, endLat: Double, endLng: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(startLat, startLng, endLat, endLng, results)
        return results[0]
    }
}