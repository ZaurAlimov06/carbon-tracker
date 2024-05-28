package com.carbon.tracker.ui.model

import androidx.annotation.DrawableRes
import com.carbon.tracker.R
import com.carbon.tracker.ui.route.Route

sealed class BottomBarType(
    @DrawableRes val icon: Int,
    val screenRoute: Route
) {
    object Search : BottomBarType(R.drawable.ic_bnv_search, Route.SCREEN_INFO)
    object Goals : BottomBarType(R.drawable.ic_bnv_goals, Route.SCREEN_HOME)
    object Profile : BottomBarType(R.drawable.ic_bnv_profile, Route.SCREEN_PROFILE)
}

val bottomNavigationItems: List<BottomBarType> = listOf(
    BottomBarType.Search,
    BottomBarType.Goals,
    BottomBarType.Profile
)
