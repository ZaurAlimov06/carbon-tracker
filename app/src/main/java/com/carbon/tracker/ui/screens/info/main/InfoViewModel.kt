package com.carbon.tracker.ui.screens.info.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carbon.tracker.data.remote.dto.Consumption
import com.carbon.tracker.ui.model.ConsumptionConstants
import com.carbon.tracker.ui.model.ExceptionHandler
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
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(InfoUiState())
    val uiState: StateFlow<InfoUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(homeUiEvent: InfoUiEvent) {
        when (homeUiEvent) {
            InfoUiEvent.GetAllConsumptions -> {
                getUserConsumptionInfo()
            }

            InfoUiEvent.OnShowChart -> {
                onShowChart()
            }
        }
    }

    private fun getUserConsumptionInfo() {
        viewModelScope.launch(ExceptionHandler.handler) {
            mainRepository.getAllConsumption()
                .collect { result ->
                    when (result) {
                        is Response.Fail -> {
                            _uiEvent.send(
                                UiEvent.ShowShortToast(message = "Failed to get consumptions.")
                            )
                        }

                        is Response.Success -> {
                            val data = result.data

                            data.let {
                                val sortedData = data.sortedByDescending {
                                    val dateList = it.date.split("-")
                                    var dateString = ""
                                    for (index in dateList.indices.reversed()) {
                                        dateString += dateList[index]
                                    }
                                    dateString
                                }
                                onUpdateDataList(dataList = sortedData)
                                onUpdateSum(calculateSum(dataList = sortedData))
                            }
                        }

                        else -> {}
                    }
                }
        }
    }

    private fun onShowChart() {
        viewModelScope.launch(ExceptionHandler.handler) {
            _uiEvent.send(
                UiEvent.Navigate(
                    navigationType = NavigationType.Navigate(Route.SCREEN_CHART.name),
                    data = mapOf(
                        "dataList" to _uiState.value.dataList
                    )
                )
            )
        }
    }

    private fun calculateSum(dataList: List<Consumption>): String {
        var sum = 0.0
        dataList.forEach {
            sum += calculateEmission(it)
        }
        return String.format("%.2f", (sum / 1000))
    }

    private fun calculateEmission(consumption: Consumption): Double {
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

    private fun onUpdateDataList(dataList: List<Consumption>) {
        viewModelScope.launch(ExceptionHandler.handler) {
            _uiState.update { currentState ->
                currentState.copy(
                    dataList = dataList
                )
            }
        }
    }

    private fun onUpdateSum(sum: String) {
        viewModelScope.launch(ExceptionHandler.handler) {
            _uiState.update { currentState ->
                currentState.copy(
                    sumOfConsumption = sum
                )
            }
        }
    }
}