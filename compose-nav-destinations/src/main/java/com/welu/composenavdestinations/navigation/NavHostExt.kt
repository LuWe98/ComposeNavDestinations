package com.welu.composenavdestinations.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.welu.composenavdestinations.spec.NavDestinationSpec
import com.welu.composenavdestinations.spec.NavDestinationPlainSpec

@Composable
fun NavHost(
    navController: NavHostController,
    startDestination: NavDestinationPlainSpec,
    modifier: Modifier = Modifier,
    route: NavDestinationSpec<*>? = null,
    builder: NavGraphBuilder.() -> Unit
) = androidx.navigation.compose.NavHost(
    navController = navController,
    startDestination = startDestination.baseRoute,
    modifier = modifier,
    route = route?.route,
    builder = builder
)