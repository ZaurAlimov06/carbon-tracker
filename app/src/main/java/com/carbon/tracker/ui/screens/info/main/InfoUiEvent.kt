package com.carbon.tracker.ui.screens.info.main

sealed class InfoUiEvent {
    object GetAllConsumptions : InfoUiEvent()
    object OnShowChart : InfoUiEvent()
}
