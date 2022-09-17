package com.welu.composenavdestinations.navigation.spec

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry

interface ArgNavGraphSpec<Arg: Any>: NavGraphSpec  {

    /**
     * Contains the [NamedNavArgument]s needed to instantiate this [NavGraphSpec]
     */
    val arguments: List<NamedNavArgument> get() = listOf()

    /**
     * Contains only the names of the [NamedNavArgument]s
     */
    val argumentNameList get() = arguments.map(NamedNavArgument::name)

    /**
     * Retrieves the NavArgs with an [NavBackStackEntry]
     */
    fun argsFrom(navBackStackEntry: NavBackStackEntry): Arg

}