package com.pulse.plannex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pulse.plannex.core.di.AppContainer
import com.pulse.plannex.features.auth.presentation.screens.LoginScreen
import com.pulse.plannex.features.auth.presentation.screens.RegisterScreen
import com.pulse.plannex.features.auth.presentation.viewmodel.LoginViewModel
import com.pulse.plannex.features.auth.presentation.viewmodel.RegisterViewModel
import com.pulse.plannex.features.event.di.ObjectsModule
import com.pulse.plannex.features.event.presentation.screens.ObjectsScreen
import com.pulse.plannex.ui.theme.PlannexTheme

class MainActivity : ComponentActivity() {
    private lateinit var appContainer: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appContainer = AppContainer()
        val objectsModule = ObjectsModule(appContainer)

        setContent {
            PlannexTheme {
                var currentScreen by rememberSaveable { mutableStateOf("login") }

                when (currentScreen) {
                    "login" -> {
                        val loginViewModel: LoginViewModel = viewModel {
                            LoginViewModel(appContainer.eventApi)
                        }
                        LoginScreen(
                            viewModel = loginViewModel,
                            onLoginSuccess = {
                                currentScreen = "eventos"
                            },
                            onNavigateToRegister = {
                                currentScreen = "registro"
                            }
                        )
                    }
                    "registro" -> {
                        val registerViewModel: RegisterViewModel = viewModel {
                            RegisterViewModel(appContainer.eventApi)
                        }
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
                    "eventos" -> {
                        ObjectsScreen(objectsModule.provideObjectsViewModelFactory())
                    }
                }
            }
        }
    }
}