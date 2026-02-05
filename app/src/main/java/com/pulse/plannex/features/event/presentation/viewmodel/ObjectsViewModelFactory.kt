package com.pulse.plannex.features.event.presentation.viewmodel

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.pulse.plannex.features.event.domain.usecases.PostEventUseCase;

class ObjectsViewModelFactory(
    private val postCharacterUseCase: PostEventUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ObjectsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ObjectsViewModel(postCharacterUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}