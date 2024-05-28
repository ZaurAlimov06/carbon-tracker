package com.carbon.tracker.ui.model

import com.carbon.tracker.ui.route.NavigationType

sealed class UiEvent {
    data class Navigate<T>(val navigationType: NavigationType, val data: Map<String, T?>? = null) :
        UiEvent()

    data class NavigateOnScreen(val state: Boolean) : UiEvent()
    data class ShowShortToast(val message: String?) : UiEvent()
    data class ShowLongToast(val message: String?) : UiEvent()
    data class ChangeTheme(val isDark: Boolean) : UiEvent()
    object ShowLoading : UiEvent()
    object HideLoading : UiEvent()
}