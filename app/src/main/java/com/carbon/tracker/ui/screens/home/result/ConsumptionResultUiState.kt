package com.carbon.tracker.ui.screens.home.result

import com.carbon.tracker.ui.model.RecommendationFoodType
import com.carbon.tracker.ui.model.RecommendationTransportType

data class ConsumptionResultUiState(
    val consumption: String = "",
    val foodRecommendation: RecommendationFoodType = RecommendationFoodType.NON_MEAT,
    val transportRecommendation: RecommendationTransportType = RecommendationTransportType.BIKE
)