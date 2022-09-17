package com.welu.composenavdestinations.navigation.spec

import com.welu.composenavdestinations.navigation.destinations.Destination
import com.welu.composenavdestinations.navigation.scope.DestinationScope

/**
 * Defines generated arguments of an annotated Destination
 */
sealed interface DestinationSpec<D: Destination<out DestinationScope>>: NavComponentSpec {

    /**
     * Overrides the [parentNavGraphSpec] of [NavComponentSpec] to make it non nullable since a [DestinationSpec] does always have a parent [NavGraphSpec]
     */
    override val parentNavGraphSpec: NavGraphSpec

    /**
     * The [com.welu.composenavdestinations.navigation.destinations.Destination] this [DestinationSpec] is connected with.
     */
    val destination: D

}