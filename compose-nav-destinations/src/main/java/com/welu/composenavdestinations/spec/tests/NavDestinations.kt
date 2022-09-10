package com.welu.composenavdestinations.spec.tests

import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.*

//TODO -> Hier irgenwie weitermachen und schauen was die beste möglichkeit ist das einzubauen

interface Routable {
    val parameterizedRoute: String
}


//---------------------------------------------------------------
//DESTINATIONS

sealed interface Destination <Scope: DestinationScope> {
    val content : @Composable Scope.() -> Unit
}

interface PlainDestination: Destination<PlainDestinationScope>, Routable {
    /**
     * Has to overwritten when the route of the Destination is changed in the NavDestinationDefinition
     *
     * Example: NavDestinationDefinition(route = "home") -> parameterizedRoute = "home"
     */
    override val parameterizedRoute: String get() = extractRouteName(this::class.simpleName!!)

    /**
     * Composables can be written inside of this typealias Scope Definition
     */
    override val content: PlainCompositionScope
}

//TODO Wegen der Route Testen:
// Wenn route angegeben wird extension function erstellt mit dem gleichen namen, die dann stattdessen aufgerufen wird

interface ArgDestination<Arg : Any> : Destination<ArgDestinationScope<Arg>> {

    /**
     * Composables can be written inside of this typealias Scope Definition. It is easy to get hold of the parsed Arg of the Destination
     */
    override val content: ArgCompositionScope<Arg>
}


//---------------------------------------------------------------
//SCOPES

/**
 * Defines the main components inside a Destination content Block
 */
sealed interface DestinationScope {

    val backStackEntry: NavBackStackEntry

    val navController: NavController

}


data class ArgDestinationScope<Arg : Any>(
    override val navController: NavController,
    override val backStackEntry: NavBackStackEntry,
    private val lazyArgs: Lazy<Arg>
) : DestinationScope {

    val args: Arg by lazyArgs

}

typealias ArgCompositionScope<Arg> = @Composable ArgDestinationScope<Arg>.() -> Unit


data class PlainDestinationScope(
    override val navController: NavController,
    override val backStackEntry: NavBackStackEntry
) : DestinationScope

typealias PlainCompositionScope = @Composable PlainDestinationScope.() -> Unit



//----------------------------------------------------------------------------
//SPEC

sealed interface DestinationSpec<D : Destination<out DestinationScope>> {

    val route: String

    val baseRoute: String

    val deepLinks: List<NavDeepLink> get() = emptyList()

    val destination: D

}

interface PlainDestinationSpec<D : PlainDestination> : DestinationSpec<D>, Routable {

    override val route: String get() = baseRoute

    override val parameterizedRoute: String get() = baseRoute

}

interface ArgDestinationSpec<D : ArgDestination<Arg>, Arg : Any> : DestinationSpec<D> {

    val arguments: List<NamedNavArgument> get() = listOf()

    fun argsFrom(entry: NavBackStackEntry): Arg? = null

    fun argsFrom(savedStateHandle: SavedStateHandle): Arg? = null

}



//--------------------------------------------------------------------------------
//ANNOTATIONS

@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class NavGraphDefinition(
    val start: Boolean = false
)

@Target(AnnotationTarget.CLASS)
annotation class NavDestinationDefinition(
    val route: String = "",
    val destinationType: String = "",
    val deepLinks: Array<String> = []
)

// --------------------------------------------------------------------------------
//HELPER METHODS

//TODO -> Einheitlichen Weg erstellen, einen Route Name zu erzeugen
fun extractRouteName(simpleName: String): String {
    return simpleName
//    val index = simpleName.indexOf("Destination", ignoreCase = true)
//    if(simpleName.length == index + "Destination".length) {
//
//    } else {
//
//    }
}
