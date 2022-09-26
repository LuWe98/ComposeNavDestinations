package com.welu.composenavdestinations.navigation.spec

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import com.welu.composenavdestinations.navigation.destinations.ComposeArgDestination

sealed interface ComposeArgDestinationSpec<Arg: Any, D: ComposeArgDestination<Arg, *>>: ComposeDestinationSpec<D> {

    /**
     * Contains the [NamedNavArgument]s needed to instantiate this [ComposeArgDestinationSpec]
     */
    val arguments: List<NamedNavArgument> get() = listOf()

    /**
     * Retrieves the NavArgs with an [NavBackStackEntry]
     */
    fun argsFrom(navBackStackEntry: NavBackStackEntry): Arg

    /**
     * Retrieves the NavArgs with an [SavedStateHandle]
     */
    fun argsFrom(savedStateHandle: SavedStateHandle): Arg

}