package com.pulse.plannex.features.event.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pulse.plannex.features.event.domain.entities.Evento
import com.pulse.plannex.features.event.presentation.viewmodel.ObjectsViewModel
import com.pulse.plannex.features.event.presentation.viewmodel.ObjectsViewModelFactory
import java.util.UUID

@Composable
fun ObjectsScreen(
    factory: ObjectsViewModelFactory,
    onNavigateToLocation: (Evento) -> Unit = {}
) {
    val viewModel: ObjectsViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            val formTitle = if (uiState.editingEventId == null) "Crear Nuevo Evento" else "Editando Evento"
            Text(formTitle, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.eventName,
                onValueChange = { viewModel.onEventNameChanged(it) },
                label = { Text("Nombre del Evento") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.eventDate,
                onValueChange = { viewModel.onEventDateChanged(it) },
                label = { Text("Fecha (YYYY-MM-DD HH:MM:SS)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = uiState.eventLat,
                    onValueChange = { viewModel.onEventLocationChanged(it, uiState.eventLng) },
                    label = { Text("Latitud") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = uiState.eventLng,
                    onValueChange = { viewModel.onEventLocationChanged(uiState.eventLat, it) },
                    label = { Text("Longitud") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = { viewModel.submitEvento() },
                    modifier = Modifier.weight(1f).height(48.dp),
                    enabled = !uiState.isLoading
                ) {
                    val buttonText = if (uiState.editingEventId == null) "Publicar Evento" else "Guardar Cambios"
                    Text(buttonText)
                }
                AnimatedVisibility(visible = uiState.editingEventId != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = { viewModel.cancelEdit() }) {
                        Text("Cancelar")
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))

            Text("Mis Eventos", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (uiState.isLoading && uiState.eventos.isEmpty()) {
            item { Box(modifier = Modifier.fillParentMaxSize().padding(top=32.dp), contentAlignment = Alignment.Center) { CircularProgressIndicator() } }
        } else if (uiState.error != null) {
            item { Text(uiState.error!!, color = MaterialTheme.colorScheme.error) }
        } else if (uiState.eventos.isEmpty()) {
            item { Box(modifier = Modifier.fillParentMaxSize().padding(top = 32.dp), contentAlignment = Alignment.Center) { Text("Aún no tienes eventos. ¡Crea el primero!") } }
        } else {
            items(uiState.eventos, key = { it.id ?: UUID.randomUUID() }) { evento ->
                EventCard(
                    evento = evento,
                    onEdit = { viewModel.startEdit(evento) },
                    onDelete = { evento.id?.let { nonNullId -> viewModel.deleteEvento(nonNullId) } },
                    onViewLocation = { onNavigateToLocation(evento) }
                )
            }
        }
    }
}

@Composable
fun EventCard(evento: Evento, onEdit: () -> Unit, onDelete: () -> Unit, onViewLocation: () -> Unit) {
    val dateParts = evento.fecha.split(" ").getOrNull(0)?.split("-")
    val timePart = evento.fecha.split(" ").getOrNull(1)?.substring(0, 5)

    val day = dateParts?.getOrNull(2) ?: "??"
    val monthName = when (dateParts?.getOrNull(1)) {
        "01" -> "ENE"
        "02" -> "FEB"
        "03" -> "MAR"
        "04" -> "ABR"
        "05" -> "MAY"
        "06" -> "JUN"
        "07" -> "JUL"
        "08" -> "AGO"
        "09" -> "SEP"
        "10" -> "OCT"
        "11" -> "NOV"
        "12" -> "DIC"
        else -> "???"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 0.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = day, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    Text(text = monthName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    Text(text = evento.nombre, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    if (timePart != null) {
                        Text(text = "A las $timePart hs", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Column(horizontalAlignment = Alignment.End, modifier = Modifier.padding(end = 8.dp)) {
                    TextButton(onClick = onEdit) {
                        Text("Editar")
                    }
                    TextButton(onClick = onDelete) {
                        Text("Eliminar", color = MaterialTheme.colorScheme.error)
                    }
                }
            }

            if (evento.latitud != null && evento.longitud != null) {
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onViewLocation,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("📍 Rastrear Llegada")
                    }
                }
            }
        }
    }
}
