package com.pulse.plannex.features.event.di

import com.pulse.plannex.core.di.AppContainer

class ObjectsModule(
    private val appContainer: AppContainer
) {
    private fun providePostEventUseCase(): PostEventUseCase{
        return PostEventUseCase(appContainer.eventosRepository)
    }

    fun provideObjectsViewModelFactory(): ObjectsViewModelFactory{
        return ObjectsViewModelFactory(providePostEventUseCase())
    }
}