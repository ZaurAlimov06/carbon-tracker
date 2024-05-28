package com.carbon.tracker.ui.screens.home.insert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carbon.tracker.data.remote.dto.Consumption
import com.carbon.tracker.ui.model.ConsumptionConstants
import com.carbon.tracker.ui.model.ExceptionHandler
import com.carbon.tracker.ui.model.RecommendationFoodType
import com.carbon.tracker.ui.model.RecommendationTransportType
import com.carbon.tracker.ui.model.Response
import com.carbon.tracker.ui.model.UiEvent
import com.carbon.tracker.ui.repository.MainRepository
import com.carbon.tracker.ui.route.NavigationType
import com.carbon.tracker.ui.route.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ConsumptionInsertViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ConsumptionInsertUiState())
    val uiState: StateFlow<ConsumptionInsertUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(consumptionInsertUiEvent: ConsumptionInsertUiEvent) {
        when (consumptionInsertUiEvent) {
            ConsumptionInsertUiEvent.OnUpdateDateToNow -> {
                onUpdateDate()
            }

            is ConsumptionInsertUiEvent.OnUpdateTransportBus -> {
                onUpdateBus(consumptionInsertUiEvent.value)
            }

            is ConsumptionInsertUiEvent.OnUpdateTransportTram -> {
                onUpdateTram(consumptionInsertUiEvent.value)
            }

            is ConsumptionInsertUiEvent.OnUpdateTransportCar -> {
                onUpdateCar(consumptionInsertUiEvent.value)
            }

            is ConsumptionInsertUiEvent.OnUpdateTransportTrolleybus -> {
                onUpdateTrolleybus(consumptionInsertUiEvent.value)
            }

            is ConsumptionInsertUiEvent.OnUpdateTransportMetro -> {
                onUpdateMetro(consumptionInsertUiEvent.value)
            }

            is ConsumptionInsertUiEvent.OnUpdateTransportBike -> {
                onUpdateBike(consumptionInsertUiEvent.value)
            }

            is ConsumptionInsertUiEvent.OnUpdateFood -> {
                onUpdateFood(consumptionInsertUiEvent.value)
            }

            ConsumptionInsertUiEvent.OnAddConsumptionClick -> {
                onAddClick()
            }
        }
    }

    private fun onAddClick() {
        viewModelScope.launch(ExceptionHandler.handler) {
            mainRepository.insertConsumption(
                Consumption(
                    date = _uiState.value.date,
                    bus = _uiState.value.bus,
                    metro = _uiState.value.metro,
                    tram = _uiState.value.tram,
                    trolley = _uiState.value.trolleybus,
                    car = _uiState.value.car,
                    bike = _uiState.value.bike,
                    food = _uiState.value.food
                )
            ).collect { result ->
                when (result) {
                    is Response.Fail -> {
                        _uiEvent.send(
                            UiEvent.ShowShortToast(message = "Failed to add consumption.")
                        )
                    }

                    is Response.Success -> {
                        onNavigateConsumptionResult()
                    }

                    else -> {}
                }
            }
        }
    }

    private fun onNavigateConsumptionResult() {
        viewModelScope.launch(ExceptionHandler.handler) {
            _uiEvent.send(
                UiEvent.Navigate(
                    navigationType = NavigationType.Navigate(Route.SCREEN_CONSUMPTION_RESULT.name),
                    data = mapOf(
                        "consumption" to String.format("%.2f", (calculateEmission() / 1000)),
                        "transportRecommendation" to getTransportRecommendation(),
                        "foodRecommendation" to getFoodRecommendation()
                    )
                )
            )
        }
    }

    private fun calculateEmission(): Double {
        val consumption = Consumption(
            date = _uiState.value.date,
            bus = _uiState.value.bus,
            metro = _uiState.value.metro,
            tram = _uiState.value.tram,
            trolley = _uiState.value.trolleybus,
            car = _uiState.value.car,
            bike = _uiState.value.bike,
            food = _uiState.value.food
        )
        return consumption.bike.toDouble() * ConsumptionConstants.bikeEffect +
            consumption.car.toDouble() * ConsumptionConstants.carEffect +
            consumption.bus.toDouble() * ConsumptionConstants.busEffect +
            consumption.metro.toDouble() * ConsumptionConstants.metroEffect +
            consumption.tram.toDouble() * ConsumptionConstants.tramEffect +
            consumption.trolley.toDouble() * ConsumptionConstants.trolleyEffect +
            getFoodEffect(consumption.food)
    }

    private fun getFoodEffect(foodType: String): Double {
        when (foodType) {
            "Vegan" -> {
                return ConsumptionConstants.veganEffect
            }

            "Vegetarian" -> {
                return ConsumptionConstants.vegetarianEffect
            }

            "LowMeat" -> {
                return ConsumptionConstants.lowMeatEffect
            }

            "HighMeat" -> {
                return ConsumptionConstants.highMeatEffect
            }

            else -> {
                return 0.0
            }
        }
    }

    private fun getFoodRecommendation(): RecommendationFoodType {
        return if (_uiState.value.food == "HighMeat" || _uiState.value.food == "LowMeat") {
            RecommendationFoodType.MEAT
        } else {
            RecommendationFoodType.NON_MEAT
        }
    }

    private fun getTransportRecommendation(): RecommendationTransportType {
        return if (_uiState.value.car.toDouble() > 0.0) {
            RecommendationTransportType.CAR
        } else if (_uiState.value.bus.toDouble() <= 0.3 ||
            _uiState.value.metro.toDouble() <= 0.3 ||
            _uiState.value.tram.toDouble() <= 0.3 ||
            _uiState.value.trolleybus.toDouble() <= 0.3
        ) {
            RecommendationTransportType.PUBLIC
        } else {
            RecommendationTransportType.BIKE
        }
    }

    private fun onUpdateDate() {
        viewModelScope.launch(ExceptionHandler.handler) {
            _uiState.update { currentState ->
                currentState.copy(
                    date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                )
            }
        }
    }

    private fun onUpdateBus(value: String) {
        viewModelScope.launch(ExceptionHandler.handler) {
            _uiState.update { currentState ->
                currentState.copy(
                    bus = value
                )
            }
            onUpdateAddConsumptionEnabled()
        }
    }

    private fun onUpdateTram(value: String) {
        viewModelScope.launch(ExceptionHandler.handler) {
            _uiState.update { currentState ->
                currentState.copy(
                    tram = value
                )
            }
            onUpdateAddConsumptionEnabled()
        }
    }

    private fun onUpdateCar(value: String) {
        viewModelScope.launch(ExceptionHandler.handler) {
            _uiState.update { currentState ->
                currentState.copy(
                    car = value
                )
            }
            onUpdateAddConsumptionEnabled()
        }
    }

    private fun onUpdateTrolleybus(value: String) {
        viewModelScope.launch(ExceptionHandler.handler) {
            _uiState.update { currentState ->
                currentState.copy(
                    trolleybus = value
                )
            }
            onUpdateAddConsumptionEnabled()
        }
    }

    private fun onUpdateMetro(value: String) {
        viewModelScope.launch(ExceptionHandler.handler) {
            _uiState.update { currentState ->
                currentState.copy(
                    metro = value
                )
            }
            onUpdateAddConsumptionEnabled()
        }
    }

    private fun onUpdateBike(value: String) {
        viewModelScope.launch(ExceptionHandler.handler) {
            _uiState.update { currentState ->
                currentState.copy(
                    bike = value
                )
            }
            onUpdateAddConsumptionEnabled()
        }
    }

    private fun onUpdateFood(value: String) {
        viewModelScope.launch(ExceptionHandler.handler) {
            _uiState.update { currentState ->
                currentState.copy(
                    food = value
                )
            }
            onUpdateAddConsumptionEnabled()
        }
    }

    private fun onUpdateAddConsumptionEnabled() {
        viewModelScope.launch(ExceptionHandler.handler) {
            _uiState.update { currentState ->
                currentState.copy(
                    isButtonEnabled = _uiState.value.bus.isNotEmpty() &&
                        _uiState.value.metro.isNotEmpty() &&
                        _uiState.value.tram.isNotEmpty() &&
                        _uiState.value.trolleybus.isNotEmpty() &&
                        _uiState.value.car.isNotEmpty() &&
                        _uiState.value.bike.isNotEmpty() &&
                        _uiState.value.food.isNotEmpty()
                )
            }
        }
    }
}