package com.welu.composenavdestinations.extensions.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.welu.composenavdestinations.navigation.Routable
import com.welu.composenavdestinations.navigation.scope.DestinationScope
import com.welu.composenavdestinations.navigation.spec.DestinationSpec
import com.welu.composenavdestinations.navigation.spec.NavComponentSpec

fun NavController.getBackStackEntry(spec: NavComponentSpec): NavBackStackEntry? {
    runCatching { getBackStackEntry(spec.route) }.onSuccess { return it }
    return null
}

//    for (i in backQueue.lastIndex downTo 0) {
//        if (backQueue[i].destination.route == spec.route) {
//            return backQueue[i]
//        }
//    }

fun NavController.navigate(
    routable: Routable,
    builder: NavOptionsBuilder.() -> Unit = {}
) = navigate(routable.parameterizedRoute, builder)

fun DestinationScope.navigate(
    routable: Routable,
    builder: NavOptionsBuilder.() -> Unit = {}
) = navController.navigate(routable, builder)
