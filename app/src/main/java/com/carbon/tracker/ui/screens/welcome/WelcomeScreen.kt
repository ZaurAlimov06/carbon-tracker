package com.carbon.tracker.ui.screens.welcome

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carbon.tracker.R
import com.carbon.tracker.ui.model.UiEvent
import com.carbon.tracker.ui.route.NavigationType
import com.carbon.tracker.ui.screens.welcome.components.Login
import com.carbon.tracker.ui.screens.welcome.components.Register
import com.carbon.tracker.ui.screens.welcome.components.WelcomeSwitch
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
fun WelcomeScreen(
    uiStateFlow: StateFlow<WelcomeUiState>,
    uiEventFlow: Flow<UiEvent>,
    onNavigate: (NavigationType, data: Map<String, Any?>?) -> Unit,
    showShortToast: (String?) -> Unit,
    showLongToast: (String?) -> Unit,
    updateLoading: (Boolean) -> Unit,
    onEvent: (WelcomeScreenUiEvent) -> Unit,
    isLoginScreen: Boolean,
    spacing: Dimension = LocalSpacing.current
) {
    var welcomeState by remember {
        mutableStateOf(isLoginScreen)
    }

    val uiState by uiStateFlow.collectAsState()

    LaunchedEffect(true) {
        uiEventFlow.collect { event ->
            when (event) {
                is UiEvent.Navigate<*> -> {
                    onNavigate(event.navigationType, event.data)
                }

                is UiEvent.NavigateOnScreen -> {
                    welcomeState = event.state
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
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = stringResource(id = R.string.welcome_title_text_welcome),
                textAlign = TextAlign.Center,
                modifier = Modifier,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = stringResource(id = R.string.welcome_text_terms_and_policy),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 15.dp, start = 40.dp, end = 40.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            WelcomeSwitch(
                welcomeStateChange = {
                    welcomeState = it
                },
                welcomeState = welcomeState
            )

            if (welcomeState) {
                Login(
                    onLoginClick = {
                        onEvent(WelcomeScreenUiEvent.OnLoginClick)
                    },
                    onPassToggleStateChange = {
                        onEvent(WelcomeScreenUiEvent.OnUpdateLoginPassVisibility(it))
                    },
                    emailStateLogin = uiState.loginEmail,
                    onEmailValueChanged = {
                        onEvent(WelcomeScreenUiEvent.OnUpdateLoginEmail(it))
                    },
                    passwordStateLogin = uiState.loginPassword,
                    onPasswordValueChanged = {
                        onEvent(WelcomeScreenUiEvent.OnUpdateLoginPassword(it))
                    },
                    passVisibilityState = uiState.isLoginPassVisible,
                    isLoginButtonEnabled = uiState.isLoginButtonEnabled
                )
            } else {
                Register(
                    onRegisterClick = {
                        onEvent(
                            WelcomeScreenUiEvent.OnRegisterClick
                        )
                    },
                    onPassToggleStateChange = {
                        onEvent(WelcomeScreenUiEvent.OnUpdateRegisterPassVisibility(it))
                    },
                    passVisibilityState = uiState.isRegisterPassVisible,
                    onPassAgainToggleStateChange = {
                        onEvent(WelcomeScreenUiEvent.OnUpdateRegisterAgainPassVisibility(it))
                    },
                    passAgainVisibilityState = uiState.isRegisterAgainPassVisible,
                    usernameStateRegister = uiState.registerUsername,
                    onUsernameChanged = {
                        onEvent(WelcomeScreenUiEvent.OnUpdateRegisterUsername(it))
                    },
                    emailStateRegister = uiState.registerEmail,
                    onEmailValueChanged = {
                        onEvent(WelcomeScreenUiEvent.OnUpdateRegisterEmail(it))
                    },
                    passwordStateRegister = uiState.registerPassword,
                    onPasswordValueChanged = {
                        onEvent(WelcomeScreenUiEvent.OnUpdateRegisterPassword(it))
                    },
                    passwordAgainStateRegister = uiState.registerPasswordAgain,
                    onPasswordAgainValueChanged = {
                        onEvent(WelcomeScreenUiEvent.OnUpdateRegisterPasswordAgain(it))
                    },
                    isRegisterButtonEnabled = uiState.isRegisterButtonEnabled
                )
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
@Preview(showBackground = true)
fun PreviewWelcomeScreen() {
    CarbonTrackerTheme {
        WelcomeScreen(
            uiStateFlow = MutableStateFlow(
                WelcomeUiState()
            ),
            uiEventFlow = Channel<UiEvent>().receiveAsFlow(),
            showShortToast = { },
            showLongToast = { },
            updateLoading = { },
            onNavigate = { _, _ -> },
            onEvent = {},
            isLoginScreen = true
        )
    }
}


