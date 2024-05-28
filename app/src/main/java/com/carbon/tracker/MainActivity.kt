package com.carbon.tracker

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.carbon.tracker.data.remote.dto.Consumption
import com.carbon.tracker.ui.components.BottomBar
import com.carbon.tracker.ui.model.RecommendationFoodType
import com.carbon.tracker.ui.model.RecommendationTransportType
import com.carbon.tracker.ui.route.NavigationType
import com.carbon.tracker.ui.route.Route
import com.carbon.tracker.ui.route.RouteArgument
import com.carbon.tracker.ui.screens.home.insert.ConsumptionInsertScreen
import com.carbon.tracker.ui.screens.home.insert.ConsumptionInsertViewModel
import com.carbon.tracker.ui.screens.home.main.HomeScreen
import com.carbon.tracker.ui.screens.home.main.HomeViewModel
import com.carbon.tracker.ui.screens.home.result.ConsumptionResultScreen
import com.carbon.tracker.ui.screens.home.result.ConsumptionResultViewModel
import com.carbon.tracker.ui.screens.info.chart.ChartScreen
import com.carbon.tracker.ui.screens.info.chart.ChartViewModel
import com.carbon.tracker.ui.screens.info.main.InfoScreen
import com.carbon.tracker.ui.screens.info.main.InfoViewModel
import com.carbon.tracker.ui.screens.onboard.OnboardScreen
import com.carbon.tracker.ui.screens.onboard.OnboardViewModel
import com.carbon.tracker.ui.screens.profile.aboutus.AboutUsScreen
import com.carbon.tracker.ui.screens.profile.edit.EditProfile
import com.carbon.tracker.ui.screens.profile.edit.EditProfileViewModel
import com.carbon.tracker.ui.screens.profile.main.ProfileScreen
import com.carbon.tracker.ui.screens.profile.main.ProfileViewModel
import com.carbon.tracker.ui.screens.profile.settings.SettingsScreen
import com.carbon.tracker.ui.screens.profile.settings.SettingsViewModel
import com.carbon.tracker.ui.screens.profile.terms.TermsScreen
import com.carbon.tracker.ui.screens.splash.SplashScreen
import com.carbon.tracker.ui.screens.splash.SplashViewModel
import com.carbon.tracker.ui.screens.welcome.WelcomeScreen
import com.carbon.tracker.ui.screens.welcome.WelcomeViewModel
import com.carbon.tracker.ui.theme.CarbonTrackerTheme
import com.carbon.tracker.ui.theme.DarkBackgroundColor
import com.carbon.tracker.ui.theme.PrimaryColor
import com.carbon.tracker.ui.theme.WhiteColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BackHandler {
                if (isTaskRoot) {
                    finish()
                }
            }

            val navController = rememberNavController()
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentScreenRoute: Route? = Route.getRoute(backStackEntry?.destination?.route)
            val isLoading = remember {
                mutableStateOf(false)
            }

            val isThemeDark = remember {
                mutableStateOf(false)
            }

            val statusBarColor = remember {
                mutableStateOf(PrimaryColor)
            }

            val view = LocalView.current

            SideEffect {
                (view.context as Activity).window.apply {
                    window.statusBarColor = statusBarColor.value.toArgb()
                    WindowCompat.getInsetsController(this, view).isAppearanceLightStatusBars =
                        !isThemeDark.value
                }
            }

            CarbonTrackerTheme(
                darkTheme = isThemeDark.value
            ) {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    bottomBar = {
                        BottomBar(
                            route = currentScreenRoute,
                            onNavigate = { navType ->
                                navController.handleNavigation(navType, null)
                            }
                        )
                    }
                ) { paddingValues ->
                    NavHost(
                        modifier = Modifier
                            .padding(paddingValues),
                        navController = navController,
                        startDestination = Route.SCREEN_SPLASH.name
                    ) {
                        composable(Route.SCREEN_SPLASH.name) {
                            val splashViewModel: SplashViewModel = hiltViewModel()

                            SplashScreen(
                                uiEventFlow = splashViewModel.uiEvent,
                                onNavigate = { navigationType, data ->
                                    navController.handleNavigation(navigationType, data)
                                },
                                onChangeTheme = {
                                    isThemeDark.value = it
                                    statusBarColor.value = if (it) {
                                        DarkBackgroundColor
                                    } else {
                                        WhiteColor
                                    }
                                }
                            )
                        }

                        composable(Route.SCREEN_ONBOARD.name) {
                            val onboardViewModel: OnboardViewModel = hiltViewModel()

                            OnboardScreen(
                                uiEventFlow = onboardViewModel.uiEvent,
                                onNavigate = { navigationType, data ->
                                    navController.handleNavigation(navigationType, data)
                                },
                                onEvent = {
                                    onboardViewModel.onEvent(it)
                                }
                            )
                        }

                        composable(Route.SCREEN_WELCOME.name) { currentStackEntry ->
                            val welcomeViewModel: WelcomeViewModel = hiltViewModel()

                            WelcomeScreen(
                                uiStateFlow = welcomeViewModel.uiState,
                                uiEventFlow = welcomeViewModel.uiEvent,
                                onNavigate = { navigationType, data ->
                                    navController.handleNavigation(navigationType, data)
                                },
                                showShortToast = {
                                    Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT)
                                        .show()
                                },
                                showLongToast = {
                                    Toast.makeText(applicationContext, it, Toast.LENGTH_LONG).show()
                                },
                                updateLoading = {
                                    isLoading.value = it
                                },
                                onEvent = {
                                    welcomeViewModel.onEvent(it)
                                },
                                isLoginScreen = currentStackEntry.savedStateHandle.get<Boolean>(
                                    RouteArgument.ARG_WELCOME_IS_LOGIN_SCREEN.name
                                ) ?: true
                            )
                        }

                        composable(Route.SCREEN_INFO.name) {
                            val infoViewModel: InfoViewModel = hiltViewModel()

                            InfoScreen(
                                uiStateFlow = infoViewModel.uiState,
                                uiEventFlow = infoViewModel.uiEvent,
                                onNavigate = { navigationType, data ->
                                    navController.handleNavigation(navigationType, data)
                                },
                                onEvent = {
                                    infoViewModel.onEvent(it)
                                },
                                showShortToast = {
                                    Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT)
                                        .show()
                                },
                                showLongToast = {
                                    Toast.makeText(applicationContext, it, Toast.LENGTH_LONG).show()
                                },
                                updateLoading = {
                                    isLoading.value = it
                                }
                            )
                        }

                        composable(Route.SCREEN_CHART.name) {
                            val chartViewModel: ChartViewModel = hiltViewModel()

                            ChartScreen(
                                uiStateFlow = chartViewModel.uiState,
                                uiEventFlow = chartViewModel.uiEvent,
                                onNavigate = { navigationType, data ->
                                    navController.handleNavigation(navigationType, data)
                                },
                                onEvent = {
                                    chartViewModel.onEvent(it)
                                },
                                showShortToast = {
                                    Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT)
                                        .show()
                                },
                                showLongToast = {
                                    Toast.makeText(applicationContext, it, Toast.LENGTH_LONG).show()
                                },
                                updateLoading = {
                                    isLoading.value = it
                                },
                                dataList = backStackEntry?.savedStateHandle?.get<List<Consumption>>(
                                    "dataList"
                                )
                            )
                        }

                        composable(Route.SCREEN_HOME.name) {
                            val homeViewModel: HomeViewModel = hiltViewModel()

                            HomeScreen(
                                uiStateFlow = homeViewModel.uiState,
                                uiEventFlow = homeViewModel.uiEvent,
                                onNavigate = { navigationType, data ->
                                    navController.handleNavigation(navigationType, data)
                                },
                                onEvent = {
                                    homeViewModel.onEvent(it)
                                },
                                showShortToast = {
                                    Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT)
                                        .show()
                                },
                                showLongToast = {
                                    Toast.makeText(applicationContext, it, Toast.LENGTH_LONG).show()
                                },
                                updateLoading = {
                                    isLoading.value = it
                                }
                            )
                        }

                        composable(Route.SCREEN_PROFILE.name) {
                            val profileViewModel: ProfileViewModel = hiltViewModel()

                            ProfileScreen(
                                uiStateFlow = profileViewModel.uiState,
                                uiEventFlow = profileViewModel.uiEvent,
                                onNavigate = { navigationType, data ->
                                    navController.handleNavigation(navigationType, data)
                                },
                                onEvent = {
                                    profileViewModel.onEvent(it)
                                }
                            )
                        }

                        composable(Route.SCREEN_ABOUT_US.name) {
                            AboutUsScreen()
                        }

                        composable(Route.SCREEN_SETTINGS.name) {
                            val settingsViewModel: SettingsViewModel = hiltViewModel()

                            SettingsScreen(
                                uiStateFlow = settingsViewModel.uiState,
                                uiEventFlow = settingsViewModel.uiEvent,
                                onNavigate = { navigationType, data ->
                                    navController.handleNavigation(navigationType, data)
                                },
                                onChangeTheme = {
                                    isThemeDark.value = it
                                    statusBarColor.value = if (it) {
                                        DarkBackgroundColor
                                    } else {
                                        WhiteColor
                                    }
                                },
                                onEvent = {
                                    settingsViewModel.onEvent(it)
                                }
                            )
                        }

                        composable(Route.SCREEN_EDIT_PROFILE.name) {
                            val editProfileViewModel: EditProfileViewModel = hiltViewModel()

                            EditProfile(
                                uiStateFlow = editProfileViewModel.uiState,
                                uiEventFlow = editProfileViewModel.uiEvent,
                                showShortToast = {
                                    Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT)
                                        .show()
                                },
                                showLongToast = {
                                    Toast.makeText(applicationContext, it, Toast.LENGTH_LONG).show()
                                },
                                onEvent = {
                                    editProfileViewModel.onEvent(it)
                                },
                                updateLoading = {
                                    isLoading.value = it
                                },
                            )
                        }

                        composable(Route.SCREEN_TERMS.name) {
                            TermsScreen()
                        }

                        composable(Route.SCREEN_CONSUMPTION_ADD.name) {
                            val consumptionInsertViewModel: ConsumptionInsertViewModel =
                                hiltViewModel()

                            ConsumptionInsertScreen(
                                uiStateFlow = consumptionInsertViewModel.uiState,
                                uiEventFlow = consumptionInsertViewModel.uiEvent,
                                onNavigate = { navigationType, data ->
                                    navController.handleNavigation(navigationType, data)
                                },
                                showShortToast = {
                                    Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT)
                                        .show()
                                },
                                showLongToast = {
                                    Toast.makeText(applicationContext, it, Toast.LENGTH_LONG).show()
                                },
                                updateLoading = {
                                    isLoading.value = it
                                },
                                onEvent = {
                                    consumptionInsertViewModel.onEvent(it)
                                }
                            )
                        }

                        composable(Route.SCREEN_CONSUMPTION_RESULT.name) {
                            val consumptionResultViewModel: ConsumptionResultViewModel =
                                hiltViewModel()

                            ConsumptionResultScreen(
                                uiStateFlow = consumptionResultViewModel.uiState,
                                uiEventFlow = consumptionResultViewModel.uiEvent,
                                onNavigate = { navigationType, data ->
                                    navController.handleNavigation(navigationType, data)
                                },
                                showShortToast = {
                                    Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT)
                                        .show()
                                },
                                showLongToast = {
                                    Toast.makeText(applicationContext, it, Toast.LENGTH_LONG).show()
                                },
                                updateLoading = {
                                    isLoading.value = it
                                },
                                onEvent = {
                                    consumptionResultViewModel.onEvent(it)
                                },
                                consumption = backStackEntry?.savedStateHandle?.get<String>(
                                    "consumption"
                                ),
                                transportRecommendation = backStackEntry?.savedStateHandle?.get<RecommendationTransportType>(
                                    "transportRecommendation"
                                ),
                                foodRecommendation = backStackEntry?.savedStateHandle?.get<RecommendationFoodType>(
                                    "foodRecommendation"
                                )
                            )
                        }
                    }
                }

                if (isLoading.value) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(enabled = false, onClick = {})
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(50.dp),
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
        }
    }

    private fun NavHostController.handleNavigation(
        navigationType: NavigationType,
        data: Map<String, Any?>?
    ) {
        val route = when (navigationType) {
            is NavigationType.Navigate -> {
                navigationType.route
            }

            is NavigationType.ClearBackStackNavigate -> {
                navigationType.route
            }

            is NavigationType.BottomBarNavigate -> {
                navigationType.route
            }

            is NavigationType.PopBack -> {
                navigationType.route
            }
        }

        when (navigationType) {
            is NavigationType.PopBack -> {
                previousBackStackEntry?.apply {
                    data?.forEach { (key, value) ->
                        savedStateHandle.set(
                            key = key,
                            value = value
                        )
                    }
                }
                popBackStack()

            }

            else -> {
                navigate(
                    route = route
                ) {
                    when (navigationType) {
                        is NavigationType.ClearBackStackNavigate -> {
                            val popUpRoute = navigationType.popUpRoute
                            if (popUpRoute != null) {
                                popUpTo(popUpRoute)
                            } else {
                                popUpTo(0)
                            }
                        }

                        is NavigationType.BottomBarNavigate -> {
                            popUpTo(Route.SCREEN_HOME.name) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }

                        else -> {}
                    }
                }
                getBackStackEntry(route = route).apply {
                    data?.forEach { (key, value) ->
                        savedStateHandle.set(
                            key = key,
                            value = value
                        )
                    }
                }
            }
        }
    }
}

