package com.carbon.tracker.ui.screens.info.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carbon.tracker.R
import com.carbon.tracker.data.remote.dto.Consumption
import com.carbon.tracker.ui.model.UiEvent
import com.carbon.tracker.ui.route.NavigationType
import com.carbon.tracker.ui.theme.CarbonTrackerTheme
import com.carbon.tracker.ui.theme.Dimension
import com.carbon.tracker.ui.theme.LocalSpacing
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun ChartScreen(
    uiStateFlow: StateFlow<ChartUiState>,
    uiEventFlow: Flow<UiEvent>,
    onNavigate: (NavigationType, data: Map<String, Any?>?) -> Unit,
    onEvent: (ChartUiEvent) -> Unit,
    showShortToast: (String?) -> Unit,
    showLongToast: (String?) -> Unit,
    updateLoading: (Boolean) -> Unit,
    dataList: List<Consumption>?,
    spacing: Dimension = LocalSpacing.current
) {
    val uiState by uiStateFlow.collectAsState()

    val localDensity = LocalDensity.current

    var columnWidthDp by remember {
        mutableStateOf(0.dp)
    }

    LaunchedEffect(true) {
        onEvent(ChartUiEvent.OnUpdateDataList(dataList))
        uiEventFlow.collect { event ->
            when (event) {
                is UiEvent.Navigate<*> -> {
                    onNavigate(event.navigationType, event.data)
                }

                is UiEvent.ShowShortToast -> {
                    showShortToast(event.message)
                }

                is UiEvent.ShowLongToast -> {
                    showLongToast(event.message)
                }

                is UiEvent.ShowLoading -> {
                    updateLoading(true)
                }

                is UiEvent.HideLoading -> {
                    updateLoading(false)
                }

                else -> {}
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = spacing.spaceScreenHorizontalPadding,
                vertical = spacing.spaceScreenVerticalPadding
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.chart_label_title),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth(),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = spacing.spaceScreenVerticalPadding)
        ) {
            items(uiState.dataList.reversed()) {

                Column(
                    modifier = Modifier
                        .fillMaxHeight(1.0f)
                        .onGloballyPositioned { coordinates ->
                            columnWidthDp = with(localDensity) {
                                coordinates.size.height.toDp() -
                                    spacing.spaceScreenVerticalPadding * 2
                            }
                            println("onGloballyPositioned  ${columnWidthDp}")
                        },
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = String.format(
                            "%.2f %s",
                            (it.second / 1000),
                            stringResource(id = R.string.common_kg_title)
                        ),
                        modifier = Modifier,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .height(it.second.dp * (columnWidthDp / uiState.dataList.maxOf {
                                it.second
                            }.dp))
                            .width(30.dp)
                            .background(
                                color = MaterialTheme.colorScheme.secondary,
                                shape = MaterialTheme.shapes.extraSmall
                            )
                    )

                    Text(
                        text = it.first,
                        modifier = Modifier
                            .rotate(-45f),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun PreviewChartScreen() {
    CarbonTrackerTheme {
        ChartScreen(
            uiStateFlow = MutableStateFlow(
                ChartUiState()
            ),
            uiEventFlow = Channel<UiEvent>().receiveAsFlow(),
            onNavigate = { _, _ -> },
            onEvent = { },
            showShortToast = { },
            showLongToast = { },
            updateLoading = { },
            dataList = listOf(
                Consumption(
                    date = "25-05-2024",
                    bus = "1",
                    metro = "1",
                    tram = "1",
                    trolley = "1",
                    car = "1",
                    bike = "1",
                    food = "1"
                ),
                Consumption(
                    date = "25-05-2024",
                    bus = "1",
                    metro = "1",
                    tram = "1",
                    trolley = "1",
                    car = "1",
                    bike = "1",
                    food = "1"
                )
            )
        )
    }
}
