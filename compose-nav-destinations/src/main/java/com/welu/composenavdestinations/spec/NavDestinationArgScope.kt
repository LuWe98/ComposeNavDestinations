package com.welu.composenavdestinations.spec

import androidx.navigation.NavBackStackEntry

data class NavDestinationArgScope<T>(
    override val spec: NavDestinationArgSpec<T>,
    override val backStackEntry: NavBackStackEntry
): NavDestinationScope {
    val args: T by lazy { spec.getArgs(backStackEntry) }
}