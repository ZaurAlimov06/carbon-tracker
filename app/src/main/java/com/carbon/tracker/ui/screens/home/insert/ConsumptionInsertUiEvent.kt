package com.carbon.tracker.ui.screens.home.insert

sealed class ConsumptionInsertUiEvent {
    object OnUpdateDateToNow : ConsumptionInsertUiEvent()
    data class OnUpdateTransportBus(val value: String) : ConsumptionInsertUiEvent()
    data class OnUpdateTransportMetro(val value: String) : ConsumptionInsertUiEvent()
    data class OnUpdateTransportTram(val value: String) : ConsumptionInsertUiEvent()
    data class OnUpdateTransportTrolleybus(val value: String) : ConsumptionInsertUiEvent()
    data class OnUpdateTransportCar(val value: String) : ConsumptionInsertUiEvent()
    data class OnUpdateTransportBike(val value: String) : ConsumptionInsertUiEvent()
    data class OnUpdateFood(val value: String) : ConsumptionInsertUiEvent()
    object OnAddConsumptionClick : ConsumptionInsertUiEvent()
}
