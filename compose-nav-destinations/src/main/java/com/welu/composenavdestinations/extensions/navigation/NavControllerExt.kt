package com.welu.composenavdestinations.extensions.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.Navigator
import com.welu.composenavdestinations.navigation.Routable
import com.welu.composenavdestinations.navigation.scope.ComposeDestinationScope
import com.welu.composenavdestinations.navigation.spec.ComposeDestinationSpec
import com.welu.composenavdestinations.navigation.spec.NavComponentSpec

fun ComposeDestinationScope.getBackStackEntry(spec: NavComponentSpec) = navController.getBackStackEntry(spec)

fun NavController.getBackStackEntry(spec: NavComponentSpec): NavBackStackEntry? = try {
    getBackStackEntry(spec.route)
} catch (e: IllegalArgumentException) {
    null
}

fun ComposeDestinationScope.isOnBackStack(spec: NavComponentSpec) = navController.isOnBackStack(spec)

fun NavController.isOnBackStack(spec: NavComponentSpec) = getBackStackEntry(spec) != null

//    for (i in backQueue.lastIndex downTo 0) {
//        if (backQueue[i].destination.route == spec.route) {
//            return backQueue[i]
//        }
//    }


fun NavController.navigate(
    toRoutable: Routable
) = navigate(toRoutable.parameterizedRoute)

fun ComposeDestinationScope.navigate(
    toRoutable: Routable
) = navController.navigate(toRoutable)

fun NavController.navigate(
    toRoutable: Routable,
    builder: NavOptionsBuilder.() -> Unit
) = navigate(toRoutable.parameterizedRoute, builder)

fun ComposeDestinationScope.navigate(
    toRoutable: Routable,
    builder: NavOptionsBuilder.() -> Unit
) = navController.navigate(toRoutable, builder)

fun NavController.navigate(
    toRoutable: Routable,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) = navigate(toRoutable.parameterizedRoute, navOptions, navigatorExtras)

fun ComposeDestinationScope.navigate(
    toRoutable: Routable,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) = navController.navigate(toRoutable, navOptions, navigatorExtras)

fun NavController.navigateAndPopUpTo(
    toRoutable: Routable,
    popUpTo: String,
    inclusive: Boolean = true
) {
    val extras = NavOptions.Builder().setPopUpTo(popUpTo, inclusive).build()
    navigate(toRoutable.parameterizedRoute, extras)
}

fun ComposeDestinationScope.navigateAndPopUpTo(
    toRoutable: Routable,
    popUpTo: String,
    inclusive: Boolean = true
) = navController.navigateAndPopUpTo(toRoutable, popUpTo, inclusive)

fun NavController.navigateAndPopUpTo(
    toRoutable: Routable,
    popUpTo: ComposeDestinationSpec<*>,
    inclusive: Boolean = true
) = navigateAndPopUpTo(toRoutable, popUpTo.route, inclusive)

fun ComposeDestinationScope.navigateAndPopUpTo(
    toRoutable: Routable,
    popUpTo: ComposeDestinationSpec<*>,
    inclusive: Boolean = true
) = navController.navigateAndPopUpTo(toRoutable, popUpTo, inclusive)

fun NavController.popBackStack(
    toDestinationSpec: ComposeDestinationSpec<*>,
    inclusive: Boolean = false,
    saveState: Boolean = false
) = popBackStack(toDestinationSpec.route, inclusive, saveState)

fun ComposeDestinationScope.popBackStack(
    toDestinationSpec: ComposeDestinationSpec<*>,
    inclusive: Boolean = false,
    saveState: Boolean = false
) = navController.popBackStack(toDestinationSpec, inclusive, saveState)

fun ComposeDestinationScope.navigateUp() = navController.navigateUp()

fun ComposeDestinationScope.popBackStack() = navController.popBackStack()