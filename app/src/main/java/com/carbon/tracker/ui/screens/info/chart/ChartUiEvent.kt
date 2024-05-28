package com.carbon.tracker.ui.screens.info.chart

import com.carbon.tracker.data.remote.dto.Consumption

sealed class ChartUiEvent {
    data class OnUpdateDataList(val dataList: List<Consumption>?) : ChartUiEvent()
}