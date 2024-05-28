package com.carbon.tracker.ui.screens.profile.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carbon.tracker.ui.model.ExceptionHandler
import com.carbon.tracker.ui.model.Response
import com.carbon.tracker.ui.model.UiEvent
import com.carbon.tracker.ui.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        getUsername()
    }

    fun onEvent(editProfileUiEvent: EditProfileUiEvent) {
        when (editProfileUiEvent) {
            is EditProfileUiEvent.OnUpdateRemoteUsername -> {
                onUpdateRemoteUsername()
            }

            is EditProfileUiEvent.OnUpdateUsername -> {
                onUpdateUsername(editProfileUiEvent.username)
            }
        }
    }

    private fun getUsername() {
        viewModelScope.launch(ExceptionHandler.handler) {
            onUpdateUsername(mainRepository.getUsername())
        }
    }

    private fun onUpdateUsername(username: String) {
        viewModelScope.launch(ExceptionHandler.handler) {
            _uiState.update { currentState ->
                currentState.copy(
                    userName = username
                )
            }
        }
    }

    private fun onUpdateRemoteUsername() {
        viewModelScope.launch(ExceptionHandler.handler) {
            _uiEvent.send(UiEvent.ShowLoading)
            when (val result = mainRepository.updateUser(_uiState.value.userName)) {
                is Response.Fail -> {
                    _uiEvent.send(UiEvent.ShowShortToast(result.exception.message))
                    _uiEvent.send(UiEvent.HideLoading)
                }

                is Response.Success -> {
                    _uiEvent.send(
                        UiEvent.ShowLongToast("Username successfully updated!")
                    )
                    _uiEvent.send(UiEvent.HideLoading)
                }

                else -> {}
            }
        }
    }
}