package com.welu.composenavdestinations.spec

import androidx.navigation.NavBackStackEntry

data class NavDestinationArgScope<T>(
    val spec: NavDestinationArgSpec<T>,
    val backStackEntry: NavBackStackEntry
) {
    val route: String get() = spec.route
    val args: T by lazy { spec.getArgs(backStackEntry) }
}