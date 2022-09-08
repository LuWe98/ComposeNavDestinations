package com.welu.composenavdestinations.spec

import androidx.navigation.NavBackStackEntry

data class NavDestinationPlainScope(
    override val spec: NavDestinationPlainSpec,
    override val backStackEntry: NavBackStackEntry
): NavDestinationScope