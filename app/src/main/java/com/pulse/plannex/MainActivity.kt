package com.pulse.plannex

import android.os.Bundle
import android.Manifest
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.pulse.plannex.features.accessControl.presentation.screens.AccessControlScreen
import com.pulse.plannex.features.accessControl.presentation.viewmodel.AccessControlViewModel
import com.pulse.plannex.features.auth.presentation.screens.LoginScreen
import com.pulse.plannex.features.auth.presentation.screens.RegisterScreen
import com.pulse.plannex.features.auth.presentation.viewmodel.LoginViewModel
import com.pulse.plannex.features.auth.presentation.viewmodel.RegisterViewModel
import com.pulse.plannex.features.event.domain.entities.Evento
import com.pulse.plannex.features.event.presentation.screens.ObjectsScreen
import com.pulse.plannex.features.event.presentation.viewmodel.ObjectsViewModel
import com.pulse.plannex.features.location.presentation.screens.LocationScreen
import com.pulse.plannex.features.location.presentation.viewmodel.LocationViewModel
import com.pulse.plannex.features.notification.presentation.viewmodel.FCMViewModel
import com.pulse.plannex.ui.theme.PlannexTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PlannexTheme {
                var currentScreen by rememberSaveable { mutableStateOf("login") }
                var selectedEventForLocation by remember { mutableStateOf<Evento?>(null) }
                var selectedTab by rememberSaveable { mutableIntStateOf(0) }

                val objectsViewModel: ObjectsViewModel = hiltViewModel()
                val fcmViewModel: FCMViewModel = hiltViewModel()

                // Solicitar permisos de notificación en Android 13+
                val launcher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted -> }

                LaunchedEffect(Unit) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }

                if (currentScreen == "login" || currentScreen == "registro") {
                    when (currentScreen) {
                        "login" -> {
                            val loginViewModel: LoginViewModel = hiltViewModel()
                            val loginState by loginViewModel.uiState.collectAsState()

                            // Registro automático del token FCM al detectar login exitoso
                            LaunchedEffect(loginState.isSuccess) {
                                if (loginState.isSuccess && loginState.userId != null) {
                                    fcmViewModel.registerToken(loginState.userId!!)
                                    currentScreen = "main_content"
                                }
                            }

                            LoginScreen(
                                viewModel = loginViewModel,
                                activity = this,
                                onLoginSuccess = {
                                    // El LaunchedEffect manejará el flujo
                                },
                                onNavigateToRegister = {
                                    currentScreen = "registro"
                                }
                            )
                        }
                        "registro" -> {
                            val registerViewModel: RegisterViewModel = hiltViewModel()
                            RegisterScreen(
                                viewModel = registerViewModel,
                                onRegisterSuccess = {
                                    currentScreen = "login"
                                },
                                onBackToLogin = {
                                    currentScreen = "login"
                                }
                            )
                        }
                    }
                } else {
                    Scaffold(
                        bottomBar = {
                            NavigationBar {
                                NavigationBarItem(
                                    selected = selectedTab == 0,
                                    onClick = { selectedTab = 0 },
                                    icon = { Icon(Icons.Default.Event, contentDescription = "Eventos") },
                                    label = { Text("Eventos") }
                                )
                                NavigationBarItem(
                                    selected = selectedTab == 1,
                                    onClick = { selectedTab = 1 },
                                    icon = { Icon(Icons.Default.QrCodeScanner, contentDescription = "Scanner") },
                                    label = { Text("Scanner") }
                                )
                            }
                        }
                    ) { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            when (selectedTab) {
                                0 -> {
                                    if (selectedEventForLocation == null) {
                                        ObjectsScreen(
                                            viewModel = objectsViewModel,
                                            onNavigateToLocation = { evento ->
                                                selectedEventForLocation = evento
                                            }
                                        )
                                    } else {
                                        val evento = selectedEventForLocation!!
                                        BackHandler {
                                            selectedEventForLocation = null
                                        }
                                        val locationViewModel: LocationViewModel = hiltViewModel()
                                        LocationScreen(
                                            eventName = evento.nombre,
                                            eventLat = evento.latitud ?: 0.0,
                                            eventLng = evento.longitud ?: 0.0,
                                            viewModel = locationViewModel
                                        )
                                    }
                                }
                                1 -> {
                                    val accessControlViewModel: AccessControlViewModel = hiltViewModel()
                                    AccessControlScreen(
                                        viewModel = accessControlViewModel,
                                        onEventRegistered = {
                                            objectsViewModel.loadEventos()
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
