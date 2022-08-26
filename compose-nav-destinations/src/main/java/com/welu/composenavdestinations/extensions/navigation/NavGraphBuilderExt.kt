package com.welu.composenavdestinations.extensions.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.welu.composenavdestinations.scope.NavDestinationScope
import com.welu.composenavdestinations.spec.NavDestinationSpec

fun <T> NavGraphBuilder.composable(
    spec: NavDestinationSpec<T>,
    content: @Composable NavDestinationScope<T>.() -> Unit
) = composable(
    route = spec.route,
    arguments = spec.arguments,
    deepLinks = spec.deepLinks
) {
    content(NavDestinationScope(spec, it))
}