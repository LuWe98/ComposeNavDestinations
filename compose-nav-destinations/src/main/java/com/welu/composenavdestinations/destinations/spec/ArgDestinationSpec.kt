package com.welu.composenavdestinations.destinations.spec

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry


interface ArgDestinationSpec<Arg : Any> : DestinationSpec {

    val arguments: List<NamedNavArgument> get() = listOf()

    fun argsFrom(navBackStackEntry: NavBackStackEntry): Arg? = null

    fun argsFrom(savedStateHandle: SavedStateHandle): Arg? = null

}