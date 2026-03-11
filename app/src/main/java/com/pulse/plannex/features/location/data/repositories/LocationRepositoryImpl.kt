package com.pulse.plannex.features.location.data.repositories

import android.location.Location
import com.pulse.plannex.features.location.domain.entities.LocationObject
import com.pulse.plannex.features.location.domain.repositories.LocationRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LocationRepositoryImpl : LocationRepository {

    override fun getLocationUpdates(): Flow<LocationObject> = flow {
        // Simulamos el sensor de hardware GPS
        var currentLat = -12.0463
        var currentLng = -77.0310
        
        while (true) {
            emit(LocationObject(currentLat, currentLng))
            delay(3000) // Actualizamos cada 3 segundos
            // El usuario "se mueve" hacia el evento
            currentLat += 0.0001
            currentLng += 0.0001
        }
    }

    override fun getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        return results[0]
    }
}