package com.welu.composenavdestinations.scope

import androidx.navigation.NavBackStackEntry
import com.welu.composenavdestinations.spec.NavDestinationSpec

data class NavDestinationScope<T>(
    val spec: NavDestinationSpec<T>,
    val backStackEntry: NavBackStackEntry
) {
    val route: String get() = spec.route
    val args: T get() = spec.getArgs(backStackEntry)
}