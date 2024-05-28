package com.carbon.tracker.ui.screens.home.insert

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.carbon.tracker.ui.components.ConsumptionItem
import com.carbon.tracker.ui.components.ParameterDropDown
import com.carbon.tracker.ui.model.ConsumptionItemValue
import com.carbon.tracker.ui.model.ConsumptionType
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
fun ConsumptionInsertScreen(
    uiStateFlow: StateFlow<ConsumptionInsertUiState>,
    uiEventFlow: Flow<UiEvent>,
    onNavigate: (NavigationType, data: Map<String, Any?>?) -> Unit,
    showShortToast: (String?) -> Unit,
    showLongToast: (String?) -> Unit,
    updateLoading: (Boolean) -> Unit,
    onEvent: (ConsumptionInsertUiEvent) -> Unit,
    spacing: Dimension = LocalSpacing.current
) {
    val uiState by uiStateFlow.collectAsState()

    LaunchedEffect(true) {
        onEvent(ConsumptionInsertUiEvent.OnUpdateDateToNow)
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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = spacing.spaceScreenHorizontalPadding,
                vertical = spacing.spaceScreenVerticalPadding
            )
    ) {
        item {
            Row(
                modifier = Modifier
                    .border(
                        color = MaterialTheme.colorScheme.secondary,
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
                    text = stringResource(id = R.string.consumption_insert_label_date_title),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    modifier = Modifier,
                    text = uiState.date,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            ParameterDropDown(
                text = uiState.food,
                dropDownList = listOf(
                    stringResource(id = R.string.consumption_insert_label_food_vegan),
                    stringResource(id = R.string.consumption_insert_label_food_vegetarian),
                    stringResource(id = R.string.consumption_insert_label_food_lowMeat),
                    stringResource(id = R.string.consumption_insert_label_food_highMeat)
                ),
                onClick = {
                    onEvent(ConsumptionInsertUiEvent.OnUpdateFood(it))
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            ConsumptionItem(
                titleText = stringResource(id = R.string.consumption_insert_label_transport_title),
                color = MaterialTheme.colorScheme.secondary,
                dataList = listOf(
                    ConsumptionItemValue(
                        consumptionType = ConsumptionType.TEXT_DOUBLE,
                        keyValue = Pair(
                            stringResource(id = R.string.consumption_insert_label_transport_bus),
                            uiState.bus
                        ),
                        onChange = {
                            onEvent(ConsumptionInsertUiEvent.OnUpdateTransportBus(it))
                        }
                    ),
                    ConsumptionItemValue(
                        consumptionType = ConsumptionType.TEXT_DOUBLE,
                        keyValue = Pair(
                            stringResource(id = R.string.consumption_insert_label_transport_metro),
                            uiState.metro
                        ),
                        onChange = {
                            onEvent(ConsumptionInsertUiEvent.OnUpdateTransportMetro(it))
                        }
                    ),
                    ConsumptionItemValue(
                        consumptionType = ConsumptionType.TEXT_DOUBLE,
                        keyValue = Pair(
                            stringResource(id = R.string.consumption_insert_label_transport_tram),
                            uiState.tram
                        ),
                        onChange = {
                            onEvent(ConsumptionInsertUiEvent.OnUpdateTransportTram(it))
                        }
                    ),
                    ConsumptionItemValue(
                        consumptionType = ConsumptionType.TEXT_DOUBLE,
                        keyValue = Pair(
                            stringResource(id = R.string.consumption_insert_label_transport_trolleybus),
                            uiState.trolleybus
                        ),
                        onChange = {
                            onEvent(ConsumptionInsertUiEvent.OnUpdateTransportTrolleybus(it))
                        }
                    ),
                    ConsumptionItemValue(
                        consumptionType = ConsumptionType.TEXT_DOUBLE,
                        keyValue = Pair(
                            stringResource(id = R.string.consumption_insert_label_transport_car),
                            uiState.car
                        ),
                        onChange = {
                            onEvent(ConsumptionInsertUiEvent.OnUpdateTransportCar(it))
                        }
                    ),
                    ConsumptionItemValue(
                        consumptionType = ConsumptionType.TEXT_DOUBLE,
                        keyValue = Pair(
                            stringResource(id = R.string.consumption_insert_label_transport_bike),
                            uiState.bike
                        ),
                        onChange = {
                            onEvent(ConsumptionInsertUiEvent.OnUpdateTransportBike(it))
                        }
                    )
                )
            )
        }

        item {
            Button(
                onClick = {
                    onEvent(ConsumptionInsertUiEvent.OnAddConsumptionClick)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                enabled = uiState.isButtonEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Text(
                    text = stringResource(id = R.string.home_button_add_consumption),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
@Preview(showBackground = true)
fun PreviewConsumptionInsertScreen() {
    CarbonTrackerTheme {
        ConsumptionInsertScreen(
            uiStateFlow = MutableStateFlow(
                ConsumptionInsertUiState()
            ),
            uiEventFlow = Channel<UiEvent>().receiveAsFlow(),
            showShortToast = { },
            showLongToast = { },
            updateLoading = { },
            onNavigate = { _, _ -> },
            onEvent = {}
        )
    }
}


