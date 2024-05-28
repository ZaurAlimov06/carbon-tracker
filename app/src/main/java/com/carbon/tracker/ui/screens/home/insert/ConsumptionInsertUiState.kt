package com.carbon.tracker.ui.screens.home.insert

data class ConsumptionInsertUiState(
    val date: String = "",
    val bus: String = "",
    val metro: String = "",
    val tram: String = "",
    val trolleybus: String = "",
    val car: String = "",
    val bike: String = "",
    val food: String = "",
    val isButtonEnabled: Boolean = false
)