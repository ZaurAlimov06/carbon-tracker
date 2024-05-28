package com.carbon.tracker.ui.screens.home.result

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carbon.tracker.R
import com.carbon.tracker.ui.model.RecommendationFoodType
import com.carbon.tracker.ui.model.RecommendationTransportType
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
fun ConsumptionResultScreen(
    uiStateFlow: StateFlow<ConsumptionResultUiState>,
    uiEventFlow: Flow<UiEvent>,
    onNavigate: (NavigationType, data: Map<String, Any?>?) -> Unit,
    onEvent: (ConsumptionResultUiEvent) -> Unit,
    showShortToast: (String?) -> Unit,
    showLongToast: (String?) -> Unit,
    updateLoading: (Boolean) -> Unit,
    consumption: String?,
    transportRecommendation: RecommendationTransportType?,
    foodRecommendation: RecommendationFoodType?,
    spacing: Dimension = LocalSpacing.current
) {
    val uiState by uiStateFlow.collectAsState()

    LaunchedEffect(true) {
        onEvent(ConsumptionResultUiEvent.OnUpdateConsumption(consumption))
        onEvent(
            ConsumptionResultUiEvent.OnUpdateRecommendation(
                foodRecommendation,
                transportRecommendation
            )
        )
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = spacing.spaceScreenHorizontalPadding,
                vertical = spacing.spaceScreenVerticalPadding
            )
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.consumption_result_info_title),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "${uiState.consumption} ${stringResource(id = R.string.common_kg_title)}",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(id = R.string.consumption_result_label_recommendation),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            if (uiState.foodRecommendation != RecommendationFoodType.NON_MEAT) {
                Text(
                    text = when (uiState.foodRecommendation) {
                        RecommendationFoodType.MEAT -> {
                            "* ${stringResource(id = R.string.consumption_result_label_on_high_meet_usage)}"
                        }

                        RecommendationFoodType.NON_MEAT -> {
                            ""
                        }
                    },
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            if (uiState.transportRecommendation != RecommendationTransportType.BIKE) {
                Text(
                    text = when (uiState.transportRecommendation) {
                        RecommendationTransportType.CAR -> {
                            "* ${stringResource(id = R.string.consumption_result_label_on_car_usage)}"
                        }

                        RecommendationTransportType.PUBLIC -> {
                            "* ${stringResource(id = R.string.consumption_result_label_on_public_transport_usage)}"
                        }

                        RecommendationTransportType.BIKE -> {
                            ""
                        }
                    },
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Button(
            onClick = {
                onEvent(ConsumptionResultUiEvent.OnGoHomeClick)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .align(Alignment.BottomCenter),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Text(
                text = stringResource(id = R.string.consumption_result_btn_go_back_home),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewConsumptionResultScreen() {
    CarbonTrackerTheme {
        ConsumptionResultScreen(
            uiStateFlow = MutableStateFlow(
                ConsumptionResultUiState(
                    consumption = "20.5",
                    foodRecommendation = RecommendationFoodType.MEAT,
                    transportRecommendation = RecommendationTransportType.CAR
                )
            ),
            uiEventFlow = Channel<UiEvent>().receiveAsFlow(),
            onNavigate = { _, _ -> },
            onEvent = { },
            showShortToast = { },
            showLongToast = { },
            updateLoading = { },
            consumption = "",
            transportRecommendation = RecommendationTransportType.CAR,
            foodRecommendation = RecommendationFoodType.MEAT
        )
    }
}

