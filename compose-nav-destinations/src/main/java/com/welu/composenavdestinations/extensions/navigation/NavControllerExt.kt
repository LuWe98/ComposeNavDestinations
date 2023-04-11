package com.welu.composenavdestinations.extensions.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.Navigator
import com.welu.composenavdestinations.navigation.Routable
import com.welu.composenavdestinations.navigation.destinations.ComposeDestination
import com.welu.composenavdestinations.navigation.destinations.ComposeRoutableDestination
import com.welu.composenavdestinations.navigation.spec.ComposeDestinationSpec
import com.welu.composenavdestinations.navigation.spec.NavComponentSpec

val NavController.currentComposeDestination get() = currentBackStackEntry?.composeDestination

val NavController.previousComposeDestination get() = previousBackStackEntry?.composeDestination


fun NavController.findBackStackEntry(spec: NavComponentSpec): NavBackStackEntry? = try {
    getBackStackEntry(spec.route)
} catch (e: IllegalArgumentException) {
    null
}

fun NavController.findBackStackEntry(destination: ComposeDestination<*>): NavBackStackEntry? = try {
    getBackStackEntry(destination.route)
} catch (e: IllegalArgumentException) {
    null
}

fun NavController.getBackStackEntry(spec: NavComponentSpec): NavBackStackEntry = findBackStackEntry(spec) ?:
    throw IllegalStateException("Could not find the BackStackEntry for the specified ComposeDestinationSpec")

fun NavController.getBackStackEntry(destination: ComposeDestination<*>): NavBackStackEntry = findBackStackEntry(destination) ?:
    throw IllegalStateException("Could not find the BackStackEntry for the specified ComposeDestinationSpec")


fun NavController.isOnBackStack(spec: NavComponentSpec) = findBackStackEntry(spec) != null


fun NavController.navigate(
    toRoutable: Routable
) = navigate(toRoutable.parameterizedRoute)

fun NavController.navigate(
    toRoutable: Routable,
    builder: NavOptionsBuilder.() -> Unit
) = navigate(toRoutable.parameterizedRoute, builder)

fun NavController.navigate(
    toRoutable: Routable,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) = navigate(toRoutable.parameterizedRoute, navOptions, navigatorExtras)

fun NavController.navigateAndPopUpTo(
    toRoutable: Routable,
    popUpTo: String,
    inclusive: Boolean = true
) {
    val extras = NavOptions.Builder().setPopUpTo(popUpTo, inclusive).build()
    navigate(toRoutable.parameterizedRoute, extras)
}

fun NavController.navigateAndPopUpTo(
    toRoutable: Routable,
    popUpTo: ComposeDestinationSpec<*>,
    inclusive: Boolean = true
) = navigateAndPopUpTo(toRoutable, popUpTo.route, inclusive)

fun NavController.popBackStack(
    toDestinationSpec: ComposeDestinationSpec<*>,
    inclusive: Boolean = false,
    saveState: Boolean = false
) = popBackStack(toDestinationSpec.route, inclusive, saveState)

fun NavController.navigate(
    toDestination: ComposeRoutableDestination<*>
) = navigate(toDestination.route)

fun NavController.navigate(
    toDestination: ComposeRoutableDestination<*>,
    builder: NavOptionsBuilder.() -> Unit
) = navigate(toDestination.route, builder)

fun NavController.navigate(
    toDestination: ComposeRoutableDestination<*>,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) = navigate(toDestination.route, navOptions, navigatorExtras)

fun NavController.navigateAndPopUpTo(
    toDestination: ComposeRoutableDestination<*>,
    popUpTo: String,
    inclusive: Boolean = true
) {
    val extras = NavOptions.Builder().setPopUpTo(popUpTo, inclusive).build()
    navigate(toDestination.route, extras)
}

fun NavController.navigateAndPopUpTo(
    toDestination: ComposeRoutableDestination<*>,
    popUpToSpec: ComposeDestinationSpec<*>,
    inclusive: Boolean = true
) = navigateAndPopUpTo(toDestination, popUpToSpec.route, inclusive)

fun NavController.navigateAndPopUpTo(
    toDestination: ComposeRoutableDestination<*>,
    popUpToDestination: ComposeDestination<*>,
    inclusive: Boolean = true
) = navigateAndPopUpTo(toDestination, popUpToDestination.route, inclusive)

fun NavController.navigateAndPopUpTo(
    toRoutable: Routable,
    popUpTo: ComposeDestination<*>,
    inclusive: Boolean = true
) {
    val extras = NavOptions.Builder().setPopUpTo(popUpTo.route, inclusive).build()
    navigate(toRoutable.parameterizedRoute, extras)
}

fun NavController.popBackStack(
    toDestination: ComposeDestination<*>,
    inclusive: Boolean = false,
    saveState: Boolean = false
) = popBackStack(toDestination.route, inclusive, saveState)