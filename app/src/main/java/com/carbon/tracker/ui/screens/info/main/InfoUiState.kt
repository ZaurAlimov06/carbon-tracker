package com.carbon.tracker.ui.screens.info.main

import com.carbon.tracker.data.remote.dto.Consumption

data class InfoUiState(
    val dataList: List<Consumption> = listOf(),
    val sumOfConsumption: String = ""
)