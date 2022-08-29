package com.welu.composenavdestinations.spec

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry

interface NavDestinationArgSpec<T>: NavDestinationSpec {

    val arguments: List<NamedNavArgument> get() = emptyList()

    fun getArgs(navBackStackEntry: NavBackStackEntry): T

    fun getArgs(savedStateHandle: SavedStateHandle): T

}