package com.carbon.tracker.ui.screens.home.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carbon.tracker.ui.model.ExceptionHandler
import com.carbon.tracker.ui.model.RecommendationFoodType
import com.carbon.tracker.ui.model.RecommendationTransportType
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
class ConsumptionResultViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ConsumptionResultUiState())
    val uiState: StateFlow<ConsumptionResultUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(consumptionResultUiEvent: ConsumptionResultUiEvent) {
        when (consumptionResultUiEvent) {
            ConsumptionResultUiEvent.OnGoHomeClick -> {
                onGoHomeClick()
            }

            is ConsumptionResultUiEvent.OnUpdateConsumption -> {
                onUpdateConsumption(consumptionResultUiEvent.consumption)
            }

            is ConsumptionResultUiEvent.OnUpdateRecommendation -> {
                onUpdateRecommendation(
                    consumptionResultUiEvent.foodRecommendation,
                    consumptionResultUiEvent.transportRecommendation
                )
            }
        }
    }

    private fun onGoHomeClick() {
        viewModelScope.launch(ExceptionHandler.handler) {
            _uiEvent.send(
                UiEvent.Navigate(
                    navigationType = NavigationType.ClearBackStackNavigate(Route.SCREEN_HOME.name),
                    data = mapOf<String, Any>()
                )
            )
        }
    }

    private fun onUpdateConsumption(value: String?) {
        viewModelScope.launch(ExceptionHandler.handler) {
            _uiState.update { currentState ->
                currentState.copy(
                    consumption = value ?: ""
                )
            }
        }
    }

    private fun onUpdateRecommendation(
        recommendationFoodType: RecommendationFoodType?,
        recommendationTransportType: RecommendationTransportType?
    ) {
        viewModelScope.launch(ExceptionHandler.handler) {
            _uiState.update { currentState ->
                currentState.copy(
                    foodRecommendation = recommendationFoodType ?: RecommendationFoodType.NON_MEAT,
                    transportRecommendation = recommendationTransportType
                        ?: RecommendationTransportType.BIKE
                )
            }
        }
    }
}