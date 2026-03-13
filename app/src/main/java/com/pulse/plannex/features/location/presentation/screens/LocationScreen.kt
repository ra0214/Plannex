package com.pulse.plannex.features.location.presentation.screens

import android.Manifest
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
    
    val eventLocation = LatLng(eventLat, eventLng)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(eventLocation, 15f)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            viewModel.setEventData(eventName, eventLat, eventLng)
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = uiState.locationStatus != null),
                uiSettings = MapUiSettings(myLocationButtonEnabled = true)
            ) {
                Marker(
                    state = MarkerState(position = eventLocation),
                    title = eventName,
                    snippet = "Destino del evento"
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "📍 $eventName",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                uiState.locationStatus?.let { status ->
                    val distance = status.distanceToEvent
                    val distanceText = if (distance != null) {
                        if (distance > 1000) {
                            String.format(Locale.getDefault(), "%.2f km", distance / 1000)
                        } else {
                            "${distance.toInt()} metros"
                        }
                    } else "Calculando..."

                    LocationCard(
                        label = "Distancia actual",
                        value = distanceText,
                        backgroundColor = if (status.hasArrived) Color(0xFF4CAF50) else MaterialTheme.colorScheme.primaryContainer
                    )

                    if (status.hasArrived) {
                        Text(
                            text = "¡Has llegado al sitio!",
                            color = Color(0xFF2E7D32),
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                } ?: Text("Esperando señal de GPS...")
            }
        }
    }
}