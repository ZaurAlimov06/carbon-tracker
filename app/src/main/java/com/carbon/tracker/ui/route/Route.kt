package com.carbon.tracker.ui.route

enum class Route(
    val hasBottomBar: Boolean = false
) {
    SCREEN_ABOUT_US,
    SCREEN_SPLASH,
    SCREEN_ONBOARD,
    SCREEN_WELCOME,
    SCREEN_INFO(hasBottomBar = true),
    SCREEN_CHART,
    SCREEN_HOME(hasBottomBar = true),
    SCREEN_PROFILE(hasBottomBar = true),
    SCREEN_SETTINGS,
    SCREEN_EDIT_PROFILE,
    SCREEN_TERMS,
    SCREEN_CONSUMPTION_ADD,
    SCREEN_CONSUMPTION_RESULT
    ;

    companion object {
        fun getRoute(routeName: String?): Route? {
            if (routeName != null) {
                values().forEach { route: Route ->
                    if (routeName.equals(route.name, ignoreCase = true)) {
                        return route
                    }
                }
            }
            return null
        }
    }
}