package com.welu.composenavdestinations.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.welu.composenavdestinations.spec.Routable

fun NavController.navigate(
    routable: Routable,
    builder: NavOptionsBuilder.() -> Unit = {}
) = navigate(routable.parameterizedRoute, builder)