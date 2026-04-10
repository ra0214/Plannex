package com.pulse.plannex.features.location.presentation.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.pulse.plannex.features.location.presentation.components.LocationCard
import com.pulse.plannex.features.location.presentation.viewmodel.LocationViewModel
import java.util.Locale

@Composable
fun LocationScreen(
    eventName: String,
    eventLat: Double,
    eventLng: Double,
    viewModel: LocationViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    // Gestión de permisos de ubicación
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            hasLocationPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                                   permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        }
    )

    val eventLocation = remember(eventLat, eventLng) { LatLng(eventLat, eventLng) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(eventLocation, 15f)
    }

    LaunchedEffect(key1 = eventName) {
        if (!hasLocationPermission) {
            launcher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
        }
        viewModel.setEventData(eventName, eventLat, eventLng)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "📍 Ubicación del Evento",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        // Hardware de Mapas (Google Maps)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(bottom = 16.dp)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                // Solo activamos MyLocation si tenemos el permiso, para evitar el CRASH
                properties = MapProperties(isMyLocationEnabled = hasLocationPermission),
                uiSettings = MapUiSettings(zoomControlsEnabled = true, myLocationButtonEnabled = hasLocationPermission)
            ) {
                Marker(
                    state = MarkerState(position = eventLocation),
                    title = eventName,
                    snippet = "Destino del evento"
                )
            }
        }

        LocationCard(
            label = "Nombre del Evento",
            value = uiState.eventName,
            subValue = "Destino: ${String.format(Locale.getDefault(), "%.4f, %.4f", uiState.eventLat, uiState.eventLng)}"
        )

        Spacer(modifier = Modifier.height(16.dp))

        uiState.locationStatus?.let { status ->
            LocationCard(
                label = "Tu Ubicación Actual",
                value = String.format(Locale.getDefault(), "%.4f, %.4f", status.latitude, status.longitude)
            )

            Spacer(modifier = Modifier.height(16.dp))

            status.distanceToEvent?.let { distance ->
                val distanceValue = if (distance > 1000) {
                    String.format(Locale.getDefault(), "%.2f km", distance / 1000)
                } else {
                    "${distance.toInt()} metros"
                }

                LocationCard(
                    label = "Distancia al Sitio",
                    value = distanceValue,
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer
                )
            }

            if (status.hasArrived) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text(
                        text = "✅ ¡HAS LLEGADO AL SITIO!",
                        color = Color.White,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
    }
}
