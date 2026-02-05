package com.pulse.plannex.features.event.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pulse.plannex.features.event.domain.usecases.PostEventUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.pulse.plannex.features.event.presentation.screens.ObjectsUiState
import kotlinx.coroutines.flow.update

class ObjectsViewModel (
    private val useCase: PostEventUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ObjectsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadPosts()
    }

    private fun loadPosts() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = useCase.getPosts()
            _uiState.update { currentState ->
                result.fold(
                    onSuccess = { list ->
                        currentState.copy(isLoading = false, objects = list)
                    },
                    onFailure = { error ->
                        currentState.copy(isLoading = false, error = error.message)
                    }
                )
            }
        }
    }

    fun createNewPost(title: String, body: String) {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val result = useCase.createPost(title, body)
            _uiState.update { currentState ->
                result.fold(
                    onSuccess = { newPost ->
                        currentState.copy(
                            isLoading = false,
                            objects = listOf(newPost) + currentState.objects
                        )
                    },
                    onFailure = { error ->
                        currentState.copy(isLoading = false, error = error.message)
                    }
                )
            }
        }
    }
}