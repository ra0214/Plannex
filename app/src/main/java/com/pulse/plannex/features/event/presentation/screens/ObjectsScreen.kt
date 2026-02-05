package com.pulse.plannex.features.event.presentation.screens

package com.innovatech.jsonplaceholder.features.placerholder.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.innovatech.jsonplaceholder.features.placerholder.presentation.components.ObjectCard
import com.innovatech.jsonplaceholder.features.placerholder.presentation.viewmodel.ObjectsViewModel
import com.innovatech.jsonplaceholder.features.placerholder.presentation.viewmodel.ObjectsViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ObjectsScreen(
    factory: ObjectsViewModelFactory
) {
    val viewModel: ObjectsViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("JsonPlaceHolder", fontWeight = FontWeight.ExtraBold) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Formulario para crear nuevo Post
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Crear Nuevo Post",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = body,
                    onValueChange = { body = it },
                    label = { Text("Cuerpo") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        viewModel.createNewPost(title, body)
                        title = ""
                        body = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = title.isNotBlank() && body.isNotBlank() && !uiState.isLoading
                ) {
                    Text("Publicar")
                }
            }

            // Indicador de carga central solo si la lista está vacía
            if (uiState.isLoading && uiState.objects.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            // Mensaje de error
            if (uiState.error != null) {
                Text(
                    text = uiState.error ?: "Error",
                    modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally),
                    color = Color.Red
                )
            }

            // Lista de Posts
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(uiState.objects) { post ->
                    ObjectCard(
                        id = post.id,
                        userId = post.userId,
                        title = post.title,
                        body = post.body
                    )
                }
            }
        }
    }
}