package com.pulse.plannex.features.location.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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

    LaunchedEffect(key1 = eventName) {
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
        
        Spacer(modifier = Modifier.height(24.dp))

        LocationCard(
            label = "Nombre del Evento",
            value = uiState.eventName,
            subValue = "Destino: ${String.format(Locale.getDefault(), "%.4f, %.4f", uiState.eventLat, uiState.eventLng)}"
        )

        Spacer(modifier = Modifier.height(16.dp))

        uiState.locationStatus?.let { status ->
            LocationCard(
                label = "Tu Ubicación Actual (Hardware 3)",
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
                Spacer(modifier = Modifier.height(32.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50)),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                ) {
                    Box(
                        modifier = Modifier.padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✅ ¡HAS LLEGADO AL SITIO!",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }
    }
}