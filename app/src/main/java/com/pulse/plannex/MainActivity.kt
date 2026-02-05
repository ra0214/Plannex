package com.pulse.plannex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.pulse.plannex.core.di.AppContainer
import com.pulse.plannex.features.event.di.ObjectsModule
import com.pulse.plannex.features.event.presentation.screens.ObjectsScreen
import com.pulse.plannex.ui.theme.PlannexTheme

class MainActivity : ComponentActivity() {
    private lateinit var appContainer: AppContainer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContainer = AppContainer()
        val objectsModule = ObjectsModule(appContainer)

        enableEdgeToEdge()
        setContent {
            PlannexTheme {
                ObjectsScreen(objectsModule.provideObjectsViewModelFactory())
            }
        }
    }
}