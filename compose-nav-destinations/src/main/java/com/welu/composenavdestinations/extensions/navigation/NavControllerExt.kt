package com.welu.composenavdestinations.extensions.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.welu.composenavdestinations.spec.NavDestinationPlainSpec
import com.welu.composenavdestinations.spec.Routable

fun NavController.navigate(
    routable: Routable,
    builder: NavOptionsBuilder.() -> Unit = {}
) = navigate(routable.parameterizedRoute, builder)

fun NavController.navigate(
    destination: NavDestinationPlainSpec,
    builder: NavOptionsBuilder.() -> Unit = {}
) = navigate(destination.route, builder)