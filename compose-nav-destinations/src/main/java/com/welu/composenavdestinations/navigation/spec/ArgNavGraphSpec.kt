package com.welu.composenavdestinations.navigation.spec

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry

interface ArgNavGraphSpec<Arg: Any>: ComposeNavGraphSpec  {

    /**
     * Contains the [NamedNavArgument]s needed to instantiate this [ComposeNavGraphSpec]
     */
    val arguments: List<NamedNavArgument> get() = listOf()

    /**
     * Retrieves the NavArgs with an [NavBackStackEntry]
     */
    fun argsFrom(navBackStackEntry: NavBackStackEntry): Arg

}