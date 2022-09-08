package com.welu.composenavdestinations.extensions.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.welu.composenavdestinations.spec.NavDestinationRoute

fun NavController.navigate(
    navDestinationRoute: NavDestinationRoute,
    builder: NavOptionsBuilder.() -> Unit = {}
) = navigate(navDestinationRoute.parameterizedRoute, builder)