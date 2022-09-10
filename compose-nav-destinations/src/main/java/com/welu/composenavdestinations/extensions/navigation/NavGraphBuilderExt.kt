package com.welu.composenavdestinations.extensions.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import com.welu.composenavdestinations.spec.NavDestinationArgScope
import com.welu.composenavdestinations.spec.NavDestinationArgSpec
import com.welu.composenavdestinations.spec.NavDestinationPlainScope
import com.welu.composenavdestinations.spec.NavDestinationPlainSpec

fun <T> NavGraphBuilder.navDestination(
    argsSpec: NavDestinationArgSpec<T>,
    content: @Composable NavDestinationArgScope<T>.() -> Unit
) = composable(
    route = argsSpec.route,
    arguments = argsSpec.arguments,
    deepLinks = argsSpec.deepLinks,
) {
    content(NavDestinationArgScope(argsSpec, it))
}

//Noch einbauen, dass man über einen type beim NavDestination bestimmen kann ob es ein dialog ist oder nicht
fun <T> NavGraphBuilder.navDestinationDialog(
    argsSpec: NavDestinationArgSpec<T>,
    content: @Composable NavDestinationArgScope<T>.() -> Unit
) = dialog(
    route = argsSpec.route,
    arguments = argsSpec.arguments,
    deepLinks = argsSpec.deepLinks
) {
    content(NavDestinationArgScope(argsSpec, it))
}

fun NavGraphBuilder.navDestination(
    plainSpec: NavDestinationPlainSpec,
    content: @Composable NavDestinationPlainScope.() -> Unit
) = composable(
    route = plainSpec.route,
    deepLinks = plainSpec.deepLinks
) {
    content(NavDestinationPlainScope(plainSpec, it))
}