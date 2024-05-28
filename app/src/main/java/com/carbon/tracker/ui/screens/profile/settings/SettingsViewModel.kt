package com.carbon.tracker.ui.screens.profile.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carbon.tracker.ui.model.ExceptionHandler
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
class SettingsViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        getThemeState()
    }

    fun onEvent(settingsUiEvent: SettingsUiEvent) {
        when (settingsUiEvent) {
            is SettingsUiEvent.OnChangeTheme -> {
                onChangeTheme(settingsUiEvent.isDark)
            }
        }
    }

    private fun getThemeState() {
        viewModelScope.launch(ExceptionHandler.handler) {
            _uiState.update { currentState ->
                currentState.copy(
                    themeState = mainRepository.getTheme()
                )
            }
        }
    }

    private fun onChangeTheme(isDark: Boolean) {
        viewModelScope.launch(ExceptionHandler.handler) {
            mainRepository.saveTheme(isDark)

            _uiEvent.send(
                UiEvent.ChangeTheme(
                    isDark
                )
            )

            _uiState.update { currentState ->
                currentState.copy(
                    themeState = isDark
                )
            }
        }
    }
}