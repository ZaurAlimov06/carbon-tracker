package com.carbon.tracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.carbon.tracker.R
import com.carbon.tracker.ui.model.bottomNavigationItems
import com.carbon.tracker.ui.route.NavigationType
import com.carbon.tracker.ui.route.Route
import com.carbon.tracker.ui.theme.UnselectColor

@Composable
fun BottomBar(
    route: Route?,
    onNavigate: (NavigationType) -> Unit
) {

    if (route?.hasBottomBar == true) {
        NavigationBar(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.background
                ),
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            tonalElevation = 0.dp
        ) {

            bottomNavigationItems.forEach { item ->
                NavigationBarItem(
                    enabled = true,
                    icon = {
                        Icon(
                            modifier = Modifier,
                            painter = painterResource(id = item.icon),
                            contentDescription = stringResource(id = R.string.common_icon_content_description)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.secondary,
                        indicatorColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = UnselectColor,
                    ),
                    alwaysShowLabel = false,
                    selected = route == item.screenRoute,
                    onClick = {
                        onNavigate(NavigationType.BottomBarNavigate(item.screenRoute.name))
                    }
                )
            }
        }
    }
}