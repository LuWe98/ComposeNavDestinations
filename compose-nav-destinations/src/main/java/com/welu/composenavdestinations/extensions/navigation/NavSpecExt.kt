package com.welu.composenavdestinations.extensions.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import com.welu.composenavdestinations.navigation.spec.ArgDestinationSpec
import com.welu.composenavdestinations.navigation.spec.ArgNavGraphSpec
import com.welu.composenavdestinations.navigation.spec.NavComponentSpec
import com.welu.composenavdestinations.navigation.spec.NavGraphSpec

//TODO -> Nochmal schauen
fun NavComponentSpec.isInsideGraph(navGraph: NavGraphSpec): Boolean {
    if(this == navGraph || this.parentNavGraphSpec == navGraph) {
        return true
    }

    if(navGraph.childNavComponentSpecs.any { it == this }) {
        return true
    }

    return navGraph.childNavComponentSpecs.any { it.isInsideGraph(navGraph) }
}


fun <Arg: Any> ArgDestinationSpec<Arg>.areArgumentsSetCorrectly(navBackStackEntry: NavBackStackEntry) = arguments.areArgumentsSetCorrectly(navBackStackEntry)

fun <Arg: Any> ArgDestinationSpec<Arg>.areArgumentsSetCorrectly(savedStateHandle: SavedStateHandle) = arguments.areArgumentsSetCorrectly(savedStateHandle)

fun <Arg: Any> ArgNavGraphSpec<Arg>.areArgumentsSetCorrectly(savedStateHandle: SavedStateHandle) = arguments.areArgumentsSetCorrectly(savedStateHandle)

/**
 * Validates if all [NamedNavArgument]s are set correctly in this [NavBackStackEntry].
 * If not then the navigation to this [NavComponentSpec] was forwarded from a [NavGraphSpec].
 */
private fun List<NamedNavArgument>.areArgumentsSetCorrectly(navBackStackEntry: NavBackStackEntry): Boolean = all {
    if(navBackStackEntry.arguments?.containsKey(it.name) == true) {
        navBackStackEntry.arguments?.get(it.name) != null || it.argument.isNullable
    } else {
        it.argument.isNullable
    }
}

/**
 * Validates if all [NamedNavArgument]s are set correctly in this [SavedStateHandle].
 * If not then the navigation to this [NavComponentSpec] was forwarded from a [NavGraphSpec].
 */
private fun List<NamedNavArgument>.areArgumentsSetCorrectly(savedStateHandle: SavedStateHandle): Boolean = all {
    if(savedStateHandle.keys().contains(it.name)) {
        savedStateHandle.get<Any?>(it.name) != null || it.argument.isNullable
    } else {
        it.argument.isNullable
    }
}