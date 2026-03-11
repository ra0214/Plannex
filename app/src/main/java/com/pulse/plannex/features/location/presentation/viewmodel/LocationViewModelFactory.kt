package com.pulse.plannex.features.location.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pulse.plannex.features.location.domain.usecases.GetLocationUpdatesUseCase

class LocationViewModelFactory(
    private val getLocationUpdatesUseCase: GetLocationUpdatesUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LocationViewModel(getLocationUpdatesUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}