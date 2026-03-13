package com.pulse.plannex.features.location.data.repositories

import android.annotation.SuppressLint
import android.os.Looper
import com.google.android.gms.location.*
import com.pulse.plannex.features.location.domain.entities.LocationObject
import com.pulse.plannex.features.location.domain.repositories.LocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class LocationRepositoryImpl(
    private val fusedLocationClient: FusedLocationProviderClient
) : LocationRepository {

    @SuppressLint("MissingPermission")
    override fun getLocationUpdates(): Flow<LocationObject> = callbackFlow {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
            .setMinUpdateIntervalMillis(2000)
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    trySend(LocationObject(location.latitude, location.longitude))
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )

        awaitClose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    override fun getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val results = FloatArray(1)
        android.location.Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        return results[0]
    }

    override suspend fun getRouteDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        return withContext(Dispatchers.IO) {
            try {
                // NOTA: Para que esto funcione, necesitas una API KEY con "Directions API" activada.
                // Reemplaza TU_API_KEY por una real.
                val apiKey = "TU_API_KEY_AQUI"
                val url = "https://maps.googleapis.com/maps/api/directions/json?origin=$lat1,$lon1&destination=$lat2,$lon2&key=$apiKey"
                
                val response = URL(url).readText()
                val json = JSONObject(response)
                val routes = json.getJSONArray("routes")
                
                if (routes.length() > 0) {
                    val legs = routes.getJSONObject(0).getJSONArray("legs")
                    val distance = legs.getJSONObject(0).getJSONObject("distance").getInt("value")
                    distance.toFloat() // Devuelve metros por carretera
                } else {
                    getDistance(lat1, lon1, lat2, lon2) // Fallback a línea recta
                }
            } catch (e: Exception) {
                getDistance(lat1, lon1, lat2, lon2) // Fallback en caso de error
            }
        }
    }
}