package com.carbon.tracker.ui.screens.profile.settings

sealed class SettingsUiEvent {
    data class OnChangeTheme(val isDark: Boolean) : SettingsUiEvent()
}