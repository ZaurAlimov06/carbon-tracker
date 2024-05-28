package com.carbon.tracker.ui.screens.welcome

sealed class WelcomeScreenUiEvent {
    object OnLoginClick : WelcomeScreenUiEvent()
    object OnRegisterClick : WelcomeScreenUiEvent()
    data class OnUpdateLoginPassVisibility(val state: Boolean) : WelcomeScreenUiEvent()
    data class OnUpdateRegisterPassVisibility(val state: Boolean) : WelcomeScreenUiEvent()
    data class OnUpdateRegisterAgainPassVisibility(val state: Boolean) : WelcomeScreenUiEvent()
    data class OnUpdateRegisterUsername(val username: String) : WelcomeScreenUiEvent()
    data class OnUpdateRegisterEmail(val email: String) : WelcomeScreenUiEvent()
    data class OnUpdateRegisterPassword(val password: String) : WelcomeScreenUiEvent()
    data class OnUpdateRegisterPasswordAgain(val passwordAgain: String) : WelcomeScreenUiEvent()
    data class OnUpdateLoginEmail(val email: String) : WelcomeScreenUiEvent()
    data class OnUpdateLoginPassword(val password: String) : WelcomeScreenUiEvent()
}