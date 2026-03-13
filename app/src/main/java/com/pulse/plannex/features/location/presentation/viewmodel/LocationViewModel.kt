package com.pulse.plannex.features.location.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pulse.plannex.features.location.domain.usecases.GetLocationUseCase
import com.pulse.plannex.features.location.presentation.screens.LocationUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LocationViewModel(
    private val getLocationUseCase: GetLocationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LocationUiState())
    val uiState = _uiState.asStateFlow()

    fun setEventData(name: String, lat: Double, lng: Double) {
        _uiState.update { it.copy(eventName = name, eventLat = lat, eventLng = lng) }
        startLocationTracking()
    }

    private fun startLocationTracking() {
        viewModelScope.launch {
            getLocationUseCase().collect { status ->
                val distance = getLocationUseCase.calculateDistance(
                    status.latitude,
                    status.longitude,
                    _uiState.value.eventLat,
                    _uiState.value.eventLng
                )
                
                // Radio de 100 metros para geofencing
                val arrived = distance < 100

                _uiState.update { state ->
                    state.copy(
                        locationStatus = status.copy(
                            distanceToEvent = distance,
                            hasArrived = arrived
                        )
                    )
                }
            }
        }
    }
}