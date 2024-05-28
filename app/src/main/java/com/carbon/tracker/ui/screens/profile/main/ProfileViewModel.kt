package com.carbon.tracker.ui.screens.profile.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carbon.tracker.ui.model.ExceptionHandler
import com.carbon.tracker.ui.model.UiEvent
import com.carbon.tracker.ui.repository.MainRepository
import com.carbon.tracker.ui.route.NavigationType
import com.carbon.tracker.ui.route.Route
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
class ProfileViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(profileUiEvent: ProfileUiEvent) {
        when (profileUiEvent) {
            ProfileUiEvent.OnAboutUsClick -> {
                onAboutUsClick()
            }

            is ProfileUiEvent.OnEditProfileClick -> {
                onEditProfileClick()
            }

            is ProfileUiEvent.OnLogOutClick -> {
                onLogOutClick()
            }

            is ProfileUiEvent.OnSettingsClick -> {
                onSettingsClick()
            }

            is ProfileUiEvent.OnTermsClick -> {
                onTermsClick()
            }

            is ProfileUiEvent.GetUsername -> {
                getUserName()
            }
        }
    }

    private fun onAboutUsClick() {
        viewModelScope.launch(ExceptionHandler.handler) {
            _uiEvent.send(
                UiEvent.Navigate(
                    navigationType = NavigationType.Navigate(Route.SCREEN_ABOUT_US.name),
                    data = mapOf<String, Any>()
                )
            )
        }
    }

    private fun onEditProfileClick() {
        viewModelScope.launch(ExceptionHandler.handler) {
            _uiEvent.send(
                UiEvent.Navigate(
                    navigationType = NavigationType.Navigate(Route.SCREEN_EDIT_PROFILE.name),
                    data = mapOf<String, Any>()
                )
            )
        }
    }

    private fun onLogOutClick() {
        viewModelScope.launch(ExceptionHandler.handler) {
            _uiEvent.send(UiEvent.ShowLoading)

            try {
                mainRepository.logoutUser()

                _uiEvent.send(
                    UiEvent.Navigate(
                        navigationType = NavigationType.ClearBackStackNavigate(Route.SCREEN_WELCOME.name),
                        data = mapOf<String, Any>()
                    )
                )

                _uiEvent.send(UiEvent.HideLoading)
            } catch (e: Exception) {
                _uiEvent.send(
                    UiEvent.ShowShortToast(e.message)
                )

                _uiEvent.send(UiEvent.HideLoading)
            }
        }
    }

    private fun onSettingsClick() {
        viewModelScope.launch(ExceptionHandler.handler) {
            _uiEvent.send(
                UiEvent.Navigate(
                    navigationType = NavigationType.Navigate(Route.SCREEN_SETTINGS.name),
                    data = mapOf<String, Any>()
                )
            )
        }
    }

    private fun onTermsClick() {
        viewModelScope.launch(ExceptionHandler.handler) {
            _uiEvent.send(
                UiEvent.Navigate(
                    navigationType = NavigationType.Navigate(Route.SCREEN_TERMS.name),
                    data = mapOf<String, Any>()
                )
            )
        }
    }

    private fun getUserName() {
        viewModelScope.launch(ExceptionHandler.handler) {
            onUpdateUsername(mainRepository.getUsername())
        }
    }

    private fun onUpdateUsername(username: String) {
        viewModelScope.launch(ExceptionHandler.handler) {
            _uiState.update { currentState ->
                currentState.copy(
                    username = username
                )
            }
        }
    }
}