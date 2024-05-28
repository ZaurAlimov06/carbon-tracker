package com.carbon.tracker.ui.screens.welcome

data class WelcomeUiState(
    val registerUsername: String = "",
    val registerEmail: String = "",
    val registerPassword: String = "",
    val registerPasswordAgain: String = "",
    val loginEmail: String = "",
    val loginPassword: String = "",
    val isLoginPassVisible: Boolean = false,
    val isRegisterPassVisible: Boolean = false,
    val isRegisterAgainPassVisible: Boolean = false,
    val isRegisterButtonEnabled: Boolean = false,
    val isLoginButtonEnabled: Boolean = false
)