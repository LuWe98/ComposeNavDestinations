package com.welu.composenavdestinations.spec

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink

interface NavDestinationSpec<T>: Route {

    val baseRoute: String

    val arguments: List<NamedNavArgument> get() = emptyList()

    val deepLinks: List<NavDeepLink> get() = emptyList()

    fun getArgs(navBackStackEntry: NavBackStackEntry): T

    fun getArgs(savedStateHandle: SavedStateHandle): T

}