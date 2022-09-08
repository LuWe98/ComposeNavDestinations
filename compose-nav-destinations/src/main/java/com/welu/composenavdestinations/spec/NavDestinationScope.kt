package com.welu.composenavdestinations.spec

import androidx.navigation.NavBackStackEntry

sealed interface NavDestinationScope {
    val spec: NavDestinationSpec
    val backStackEntry: NavBackStackEntry
    val route: String get() = spec.route
}