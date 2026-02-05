package com.pulse.plannex.features.event.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pulse.plannex.features.event.presentation.components.EventoCard
import com.pulse.plannex.features.event.presentation.viewmodel.ObjectsViewModel
import com.pulse.plannex.features.event.presentation.viewmodel.ObjectsViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ObjectsScreen(
    factory: ObjectsViewModelFactory
) {
    val viewModel: ObjectsViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var nombre by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Plannex Eventos", fontWeight = FontWeight.ExtraBold) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Crear Nuevo Evento",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del Evento") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = fecha,
                    onValueChange = { fecha = it },
                    label = { Text("Fecha (YYYY-MM-DD HH:MM:SS)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        viewModel.createNewEvento(nombre, fecha)
                        nombre = ""
                        fecha = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = nombre.isNotBlank() && fecha.isNotBlank() && !uiState.isLoading
                ) {
                    Text("Publicar Evento")
                }
            }

            if (uiState.isLoading && uiState.eventos.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            if (uiState.error != null) {
                Text(
                    text = uiState.error ?: "Error desconocido",
                    modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally),
                    color = Color.Red
                )
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(uiState.eventos) { evento ->
                    EventoCard(
                        id = evento.id,
                        nombre = evento.nombre,
                        fecha = evento.fecha
                    )
                }
            }
        }
    }
}