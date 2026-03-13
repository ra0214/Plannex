package com.pulse.plannex.features.location.di

import com.pulse.plannex.core.di.AppContainer
import com.pulse.plannex.features.location.domain.usecases.GetLocationUseCase
import com.pulse.plannex.features.location.presentation.viewmodel.LocationViewModelFactory

class LocationModule(
    private val appContainer: AppContainer
) {
    private fun provideGetLocationUseCase(): GetLocationUseCase {
        return GetLocationUseCase(appContainer.locationRepository)
    }

    fun provideLocationViewModelFactory(): LocationViewModelFactory {
        return LocationViewModelFactory(provideGetLocationUseCase())
    }
}