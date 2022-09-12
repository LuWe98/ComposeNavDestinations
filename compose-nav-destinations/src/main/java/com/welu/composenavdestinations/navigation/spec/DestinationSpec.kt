package com.welu.composenavdestinations.navigation.spec

import androidx.navigation.NavDeepLink
import com.welu.composenavdestinations.navigation.destinations.Destination
import com.welu.composenavdestinations.navigation.scope.DestinationScope

//TODO -> Destination wieder hinterlegen! mit einem Feld
/**
 * Defines generated arguments of an annotated Destination
 */
sealed interface DestinationSpec<D: Destination<out DestinationScope>>: NavGraphBuilderComponent {

    /**
     * The [NavGraphSpec] this [DestinationSpec] is contained in.
     */
    //val navGraph: NavGraphSpec

    /**
     * The [com.welu.composenavdestinations.navigation.destinations.Destination] this [DestinationSpec] is connected with.
     */
    val destination: D

    /**
     * This is the [route] without any argument definitions
     */
    val baseRoute: String

    /**
     * Contains the [NavDeepLink] to this Destination
     */
    val deepLinks: List<NavDeepLink> get() = emptyList()

}