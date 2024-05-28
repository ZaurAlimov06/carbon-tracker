package com.carbon.tracker.ui.screens.info.main

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carbon.tracker.R
import com.carbon.tracker.data.remote.dto.Consumption
import com.carbon.tracker.ui.components.ConsumptionListItem
import com.carbon.tracker.ui.model.ConsumptionItemValue
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

@ExperimentalMaterial3Api
@Composable
fun InfoScreen(
    uiStateFlow: StateFlow<InfoUiState>,
    uiEventFlow: Flow<UiEvent>,
    onNavigate: (NavigationType, data: Map<String, Any?>?) -> Unit,
    showShortToast: (String?) -> Unit,
    showLongToast: (String?) -> Unit,
    updateLoading: (Boolean) -> Unit,
    onEvent: (InfoUiEvent) -> Unit,
    spacing: Dimension = LocalSpacing.current
) {
    val uiState by uiStateFlow.collectAsState()

    LaunchedEffect(true) {
        onEvent(InfoUiEvent.GetAllConsumptions)
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
            )
    ) {
        if (uiState.sumOfConsumption.isNotBlank()) {
            Row(
                modifier = Modifier
                    .border(
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.medium,
                        width = 1.dp
                    )
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(1.0f),
                    text = stringResource(id = R.string.info_label_total_title),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    modifier = Modifier,
                    text = "${uiState.sumOfConsumption} ${stringResource(id = R.string.common_kg_title)}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        LazyColumn(
            modifier = Modifier
                .weight(1.0f)
        ) {
            items(uiState.dataList) { consumption ->
                ConsumptionListItem(
                    titleText = consumption.date,
                    color = MaterialTheme.colorScheme.primary,
                    dataList = listOf(
                        ConsumptionItemValue(
                            keyValue = Pair(
                                stringResource(id = R.string.consumption_insert_label_food_title),
                                consumption.food
                            )
                        ) {},
                        ConsumptionItemValue(
                            keyValue = Pair(
                                stringResource(id = R.string.consumption_insert_label_transport_bus),
                                consumption.bus
                            )
                        ) {},
                        ConsumptionItemValue(
                            keyValue = Pair(
                                stringResource(id = R.string.consumption_insert_label_transport_metro),
                                consumption.metro
                            )
                        ) {},
                        ConsumptionItemValue(
                            keyValue = Pair(
                                stringResource(id = R.string.consumption_insert_label_transport_tram),
                                consumption.tram
                            )
                        ) {},
                        ConsumptionItemValue(
                            keyValue = Pair(
                                stringResource(id = R.string.consumption_insert_label_transport_trolleybus),
                                consumption.trolley
                            )
                        ) {},
                        ConsumptionItemValue(
                            keyValue = Pair(
                                stringResource(id = R.string.consumption_insert_label_transport_car),
                                consumption.car
                            )
                        ) {},
                        ConsumptionItemValue(
                            keyValue = Pair(
                                stringResource(id = R.string.consumption_insert_label_transport_bike),
                                consumption.bike
                            )
                        ) {}
                    )
                )
            }
        }

        Button(
            onClick = {
                onEvent(InfoUiEvent.OnShowChart)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Text(
                text = stringResource(id = R.string.info_btn_show_chart),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
@Preview(showBackground = true)
fun PreviewInfoScreen() {
    CarbonTrackerTheme {
        InfoScreen(
            uiStateFlow = MutableStateFlow(
                InfoUiState(
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
                        )
                    )
                )
            ),
            uiEventFlow = Channel<UiEvent>().receiveAsFlow(),
            onNavigate = { _, _ -> },
            onEvent = {},
            showShortToast = { },
            showLongToast = { },
            updateLoading = { }
        )
    }
}

