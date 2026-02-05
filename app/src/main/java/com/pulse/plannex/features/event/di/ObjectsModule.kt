package com.pulse.plannex.features.event.di

import com.pulse.plannex.core.di.AppContainer
import com.pulse.plannex.features.event.domain.usecases.PostEventUseCase
import com.pulse.plannex.features.event.presentation.viewmodel.ObjectsViewModelFactory

class ObjectsModule(
    private val appContainer: AppContainer
) {
    private fun providePostEventUseCase(): PostEventUseCase{
        return PostEventUseCase(appContainer.objectRepository)
    }

    fun provideObjectsViewModelFactory(): ObjectsViewModelFactory{
        return ObjectsViewModelFactory(providePostEventUseCase())
    }
}