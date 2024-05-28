package com.carbon.tracker.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carbon.tracker.ui.model.ExceptionHandler
import com.carbon.tracker.ui.model.Response
import com.carbon.tracker.ui.model.UiEvent
import com.carbon.tracker.ui.repository.MainRepository
import com.carbon.tracker.ui.route.NavigationType
import com.carbon.tracker.ui.route.Route
import com.carbon.tracker.ui.route.RouteArgument
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        startSplashTimer()
    }

    private fun startSplashTimer() {
        viewModelScope.launch(ExceptionHandler.handler) {
            if (mainRepository.containOnboardState()) {
                if (mainRepository.containsLoginInfo()) {
                    when (val result = mainRepository.loginUser(
                        mainRepository.getEmail(),
                        mainRepository.getPassword()
                    )) {
                        is Response.Fail -> {

                        }

                        is Response.Success -> {

                        }

                        else -> {}
                    }

                    _uiEvent.send(
                        UiEvent.Navigate(
                            navigationType = NavigationType.ClearBackStackNavigate(Route.SCREEN_HOME.name),
                            data = mapOf<String, Any>()
                        )
                    )
                } else {
                    _uiEvent.send(
                        UiEvent.Navigate(
                            navigationType = NavigationType.ClearBackStackNavigate(Route.SCREEN_WELCOME.name),
                            data = mapOf(
                                RouteArgument.ARG_WELCOME_IS_LOGIN_SCREEN.name to true
                            )
                        )
                    )
                }
            } else {
                mainRepository.saveOnboardState()

                _uiEvent.send(
                    UiEvent.Navigate(
                        navigationType = NavigationType.ClearBackStackNavigate(Route.SCREEN_ONBOARD.name),
                        data = mapOf<String, Any>()
                    )
                )
            }

            onChangeTheme(mainRepository.getTheme())
        }
    }

    private fun onChangeTheme(isDark: Boolean) {
        viewModelScope.launch(ExceptionHandler.handler) {
            _uiEvent.send(
                UiEvent.ChangeTheme(isDark)
            )
        }
    }
}