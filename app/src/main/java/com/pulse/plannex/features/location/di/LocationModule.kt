package com.pulse.plannex.features.location.di

import com.pulse.plannex.core.di.AppContainer
import com.pulse.plannex.features.location.domain.usecases.GetLocationUpdatesUseCase
import com.pulse.plannex.features.location.presentation.viewmodel.LocationViewModelFactory

class LocationModule(
    private val appContainer: AppContainer
) {
    private fun provideGetLocationUpdatesUseCase(): GetLocationUpdatesUseCase {
        return GetLocationUpdatesUseCase(appContainer.locationRepository)
    }

    fun provideLocationViewModelFactory(): LocationViewModelFactory {
        return LocationViewModelFactory(provideGetLocationUpdatesUseCase())
    }
}