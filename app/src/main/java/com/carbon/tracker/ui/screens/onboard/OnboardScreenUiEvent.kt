package com.carbon.tracker.ui.screens.onboard

sealed class OnboardScreenUiEvent {
    object OnGetStartedClick : OnboardScreenUiEvent()
    object OnLogInClick : OnboardScreenUiEvent()
}