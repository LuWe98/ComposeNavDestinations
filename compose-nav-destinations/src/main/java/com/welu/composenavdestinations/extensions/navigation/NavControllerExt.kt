package com.welu.composenavdestinations.extensions.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.welu.composenavdestinations.navigation.Routable
import com.welu.composenavdestinations.navigation.scope.DestinationScope

fun NavController.navigate(
    routable: Routable,
    builder: NavOptionsBuilder.() -> Unit = {}
) = navigate(routable.parameterizedRoute, builder)

fun DestinationScope.navigate(
    routable: Routable,
    builder: NavOptionsBuilder.() -> Unit = {}
) = navController.navigate(routable, builder)
