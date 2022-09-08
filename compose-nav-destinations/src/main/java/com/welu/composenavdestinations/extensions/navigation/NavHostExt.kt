package com.welu.composenavdestinations.extensions.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.welu.composenavdestinations.spec.NavDestinationPlainSpec

@Composable
fun NavDestinationNavHost(
    startDestination: NavDestinationPlainSpec,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    route: String? = null,
    builder: NavGraphBuilder.() -> Unit
) = NavHost(
    navController = navController,
    startDestination = startDestination.baseRoute,
    modifier = modifier,
    route = route
) {
    builder()
}