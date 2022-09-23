package com.welu.composenavdestinations.navigation.spec

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import com.welu.composenavdestinations.navigation.destinations.ArgDestination

//TODO -> Beide Wege (argsFrom) sind für eine Destination eigentlich in Ordnung, da die Argumente immer vorliegen sollten -> Falsch, man kann zu einem Graphen navigieren, welcher automatisch zu startDestination weiterleitet
// Für NavArgSpecs ist es jedoch nicht in Ordnung, da man direkt zu einer Destination im Graphen navigieren kann und somit keine Args an den NavGraph übergibt.

/**
 * Describes a Destination with defines NavArgs. These can be easily obtained trough the [argsFrom] methods.
 */
interface ArgDestinationSpec<Arg : Any> : DestinationSpec<ArgDestination<Arg>> {

    /**
     * Contains the [NamedNavArgument]s needed to instantiate this [NavGraphSpec]
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