package com.welu.composenavdestinations.extensions.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.welu.composenavdestinations.spec.NavDestinationRoute
import com.welu.composenavdestinations.spec.tests.DestinationScope
import com.welu.composenavdestinations.spec.tests.Routable

fun NavController.navigate(
    navDestinationRoute: NavDestinationRoute,
    builder: NavOptionsBuilder.() -> Unit = {}
) = navigate(navDestinationRoute.parameterizedRoute, builder)

fun NavController.navigate(
    routable: Routable,
    builder: NavOptionsBuilder.() -> Unit = {}
) = navigate(routable.parameterizedRoute, builder)

fun DestinationScope.navigate(
    routable: Routable,
    builder: NavOptionsBuilder.() -> Unit = {}
) = navController.navigate(routable, builder)