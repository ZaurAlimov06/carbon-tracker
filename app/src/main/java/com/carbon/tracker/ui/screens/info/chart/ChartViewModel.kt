package com.carbon.tracker.ui.screens.info.chart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carbon.tracker.data.remote.dto.Consumption
import com.carbon.tracker.ui.model.ConsumptionConstants
import com.carbon.tracker.ui.model.ExceptionHandler
import com.carbon.tracker.ui.model.UiEvent
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
class ChartViewModel @Inject constructor(

) : ViewModel() {
    private val _uiState = MutableStateFlow(ChartUiState())
    val uiState: StateFlow<ChartUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(chartUiEvent: ChartUiEvent) {
        when (chartUiEvent) {
            is ChartUiEvent.OnUpdateDataList -> {
                onUpdateDataList(chartUiEvent.dataList)
            }
        }
    }

    private fun onUpdateDataList(dataList: List<Consumption>?) {
        viewModelScope.launch(ExceptionHandler.handler) {
            _uiState.update { currentState ->
                currentState.copy(
                    dataList = dataList?.map {
                        Pair(it.date, calculateEmission(it))
                    } ?: listOf()
                )
            }
        }
    }


    private fun calculateEmission(consumption: Consumption): Float {
        return consumption.bike.toFloat() * ConsumptionConstants.bikeEffect.toFloat() +
            consumption.car.toFloat() * ConsumptionConstants.carEffect.toFloat() +
            consumption.bus.toFloat() * ConsumptionConstants.busEffect.toFloat() +
            consumption.metro.toFloat() * ConsumptionConstants.metroEffect.toFloat() +
            consumption.tram.toFloat() * ConsumptionConstants.tramEffect.toFloat() +
            consumption.trolley.toFloat() * ConsumptionConstants.trolleyEffect.toFloat() +
            getFoodEffect(consumption.food)
    }

    private fun getFoodEffect(foodType: String): Float {
        when (foodType) {
            "Vegan" -> {
                return ConsumptionConstants.veganEffect.toFloat()
            }

            "Vegetarian" -> {
                return ConsumptionConstants.vegetarianEffect.toFloat()
            }

            "LowMeat" -> {
                return ConsumptionConstants.lowMeatEffect.toFloat()
            }

            "HighMeat" -> {
                return ConsumptionConstants.highMeatEffect.toFloat()
            }

            else -> {
                return 0.0f
            }
        }
    }
}