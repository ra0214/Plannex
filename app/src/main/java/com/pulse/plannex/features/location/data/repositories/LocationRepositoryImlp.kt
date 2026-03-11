package com.pulse.plannex.features.location.data.repositories

import android.location.Location
import com.pulse.plannex.features.location.domain.entities.LocationStatus
import com.pulse.plannex.features.location.domain.repositories.LocationRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

// Esta clase es un duplicado por error de merge. Se recomienda borrar este archivo.
class LocationRepositoryImlp : LocationRepository {

    override fun getLocationUpdates(): Flow<LocationStatus> = flow {
        var currentLat = -12.0463
        var currentLng = -77.0310
        
        while (true) {
            emit(LocationStatus(currentLat, currentLng))
            delay(3000)
            currentLat += 0.0001
            currentLng += 0.0001
        }
    }

    override fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        return results[0]
    }
}
