package com.carbon.tracker.ui.model

data class ConsumptionItemValue(
    val consumptionType: ConsumptionType = ConsumptionType.TEXT_INT,
    var keyValue: Pair<String, String>,
    val onChange: (String) -> Unit
)