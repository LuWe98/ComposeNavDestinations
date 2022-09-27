package com.welu.composenavdestinations.extensions.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import com.welu.composenavdestinations.navigation.spec.*

//TODO -> Nochmal schauen
//fun NavComponentSpec.isInsideGraph(navGraph: ComposeNavGraphSpec): Boolean {
//    return if(this == navGraph || this.parentNavGraphSpec == navGraph || navGraph.childNavComponentSpecs.any { it == this }) {
//        return true
//    } else {
//        navGraph.childNavComponentSpecs.any { it.isInsideGraph(navGraph) }
//    }
//}

fun NavComponentSpec.isInsideGraph(navGraph: ComposeNavGraphSpec): Boolean =
    this == navGraph || parentNavGraphSpec == navGraph || parentNavGraphSpec?.isInsideGraph(navGraph) == true

fun <Arg: Any> ComposeArgDestinationSpec<Arg, *>.areArgumentsSetCorrectly(navBackStackEntry: NavBackStackEntry) = arguments.areArgumentsSetCorrectly(navBackStackEntry)

fun <Arg: Any> ComposeArgDestinationSpec<Arg, *>.areArgumentsSetCorrectly(savedStateHandle: SavedStateHandle) = arguments.areArgumentsSetCorrectly(savedStateHandle)

fun <Arg: Any> ArgNavGraphSpec<Arg>.areArgumentsSetCorrectly(savedStateHandle: SavedStateHandle) = arguments.areArgumentsSetCorrectly(savedStateHandle)

/**
 * Validates if all [NamedNavArgument]s are set correctly in this [NavBackStackEntry].
 * If not then the navigation to this [NavComponentSpec] was forwarded from a [ComposeNavGraphSpec].
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
 * If not then the navigation to this [NavComponentSpec] was forwarded from a [ComposeNavGraphSpec].
 */
private fun List<NamedNavArgument>.areArgumentsSetCorrectly(savedStateHandle: SavedStateHandle): Boolean = all {
    if(savedStateHandle.keys().contains(it.name)) {
        savedStateHandle.get<Any?>(it.name) != null || it.argument.isNullable
    } else {
        it.argument.isNullable
    }
}