package com.carbon.tracker.ui.screens.profile.main


sealed class ProfileUiEvent {
    object GetUsername : ProfileUiEvent()
    object OnEditProfileClick : ProfileUiEvent()
    object OnSettingsClick : ProfileUiEvent()
    object OnAboutUsClick : ProfileUiEvent()
    object OnTermsClick : ProfileUiEvent()
    object OnLogOutClick : ProfileUiEvent()
}