package com.pulse.plannex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pulse.plannex.core.di.AppContainer
import com.pulse.plannex.features.event.di.ObjectsModule
import com.pulse.plannex.features.event.domain.entities.Evento
import com.pulse.plannex.features.event.presentation.screens.ObjectsScreen
import com.pulse.plannex.features.location.di.LocationModule
import com.pulse.plannex.features.location.presentation.screens.LocationScreen
import com.pulse.plannex.ui.theme.PlannexTheme

class MainActivity : ComponentActivity() {
    private lateinit var appContainer: AppContainer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContainer = AppContainer()
        val objectsModule = ObjectsModule(appContainer)
        val locationModule = LocationModule(appContainer)

        enableEdgeToEdge()
        setContent {
            PlannexTheme {
                var selectedEventForLocation by remember { mutableStateOf<Evento?>(null) }

                if (selectedEventForLocation == null) {
                    ObjectsScreen(
                        factory = objectsModule.provideObjectsViewModelFactory(),
                        onNavigateToLocation = { evento ->
                            selectedEventForLocation = evento
                        }
                    )
                } else {
                    val evento = selectedEventForLocation!!
                    
                    BackHandler {
                        selectedEventForLocation = null
                    }
                    
                    LocationScreen(
                        eventName = evento.nombre,
                        eventLat = evento.latitud ?: 0.0,
                        eventLng = evento.longitud ?: 0.0,
                        viewModel = viewModel(
                            factory = locationModule.provideLocationViewModelFactory()
                        )
                    )
                }
            }
        }
    }
}
