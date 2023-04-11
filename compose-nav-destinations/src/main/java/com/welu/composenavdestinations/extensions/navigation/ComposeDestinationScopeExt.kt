package com.welu.composenavdestinations.extensions.navigation

import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.Navigator
import com.welu.composenavdestinations.navigation.Routable
import com.welu.composenavdestinations.navigation.destinations.ComposeDestination
import com.welu.composenavdestinations.navigation.destinations.ComposeRoutableDestination
import com.welu.composenavdestinations.navigation.scope.ComposeDestinationScope
import com.welu.composenavdestinations.navigation.spec.ComposeDestinationSpec
import com.welu.composenavdestinations.navigation.spec.NavComponentSpec

fun ComposeDestinationScope.findBackStackEntry(spec: NavComponentSpec) = navController.findBackStackEntry(spec)

fun ComposeDestinationScope.isOnBackStack(spec: NavComponentSpec) = navController.isOnBackStack(spec)

fun ComposeDestinationScope.navigate(
    toRoutable: Routable
) = navController.navigate(toRoutable)

fun ComposeDestinationScope.navigate(
    toRoutable: Routable,
    builder: NavOptionsBuilder.() -> Unit
) = navController.navigate(toRoutable, builder)


fun ComposeDestinationScope.navigate(
    toRoutable: Routable,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) = navController.navigate(toRoutable, navOptions, navigatorExtras)


fun ComposeDestinationScope.navigateAndPopUpTo(
    toRoutable: Routable,
    popUpTo: String,
    inclusive: Boolean = true
) = navController.navigateAndPopUpTo(toRoutable, popUpTo, inclusive)


fun ComposeDestinationScope.navigateAndPopUpTo(
    toRoutable: Routable,
    popUpTo: ComposeDestinationSpec<*>,
    inclusive: Boolean = true
) = navController.navigateAndPopUpTo(toRoutable, popUpTo, inclusive)

fun ComposeDestinationScope.popBackStack(
    toDestinationSpec: ComposeDestinationSpec<*>,
    inclusive: Boolean = false,
    saveState: Boolean = false
) = navController.popBackStack(toDestinationSpec, inclusive, saveState)

fun ComposeDestinationScope.navigateUp() = navController.navigateUp()

fun ComposeDestinationScope.popBackStack() = navController.popBackStack()

fun ComposeDestinationScope.navigate(
    toDestination: ComposeRoutableDestination<*>
) = navController.navigate(toDestination)

fun ComposeDestinationScope.navigate(
    toDestination: ComposeRoutableDestination<*>,
    builder: NavOptionsBuilder.() -> Unit
) = navController.navigate(toDestination, builder)

fun ComposeDestinationScope.navigate(
    toDestination: ComposeRoutableDestination<*>,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) = navController.navigate(toDestination, navOptions, navigatorExtras)

fun ComposeDestinationScope.navigateAndPopUpTo(
    toDestination: ComposeRoutableDestination<*>,
    popUpTo: String,
    inclusive: Boolean = true
) = navController.navigateAndPopUpTo(toDestination, popUpTo, inclusive)
fun ComposeDestinationScope.navigateAndPopUpTo(
    toDestination: ComposeRoutableDestination<*>,
    popUpToSpec: ComposeDestinationSpec<*>,
    inclusive: Boolean = true
) = navController.navigateAndPopUpTo(toDestination, popUpToSpec, inclusive)
fun ComposeDestinationScope.navigateAndPopUpTo(
    toDestination: ComposeRoutableDestination<*>,
    popUpToDestination: ComposeDestination<*>,
    inclusive: Boolean = true
) = navController.navigateAndPopUpTo(toDestination, popUpToDestination.route, inclusive)
fun ComposeDestinationScope.navigateAndPopUpTo(
    toRoutable: Routable,
    popUpTo: ComposeDestination<*>,
    inclusive: Boolean = true
) = navController.navigateAndPopUpTo(toRoutable, popUpTo, inclusive)

fun ComposeDestinationScope.popBackStack(
    toDestination: ComposeDestination<*>,
    inclusive: Boolean = false,
    saveState: Boolean = false
) = navController.popBackStack(toDestination, inclusive, saveState)