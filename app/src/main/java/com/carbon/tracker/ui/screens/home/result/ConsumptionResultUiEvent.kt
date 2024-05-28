package com.carbon.tracker.ui.screens.home.result

import com.carbon.tracker.ui.model.RecommendationFoodType
import com.carbon.tracker.ui.model.RecommendationTransportType

sealed class ConsumptionResultUiEvent {
    object OnGoHomeClick : ConsumptionResultUiEvent()
    data class OnUpdateRecommendation(
        val foodRecommendation: RecommendationFoodType?,
        val transportRecommendation: RecommendationTransportType?
    ) : ConsumptionResultUiEvent()

    data class OnUpdateConsumption(val consumption: String?) : ConsumptionResultUiEvent()
}