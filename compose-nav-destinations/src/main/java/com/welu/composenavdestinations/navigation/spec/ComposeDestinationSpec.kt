package com.welu.composenavdestinations.navigation.spec

import com.welu.composenavdestinations.navigation.destinations.ComposeDestination

/**
 * Defines generated arguments of an annotated Destination
 */
sealed interface ComposeDestinationSpec<D: ComposeDestination<*>>: NavComponentSpec {

    /**
     * Overrides the [parentNavGraphSpec] of [NavComponentSpec] to make it non nullable since a [ComposeDestinationSpec] does always have a parent [ComposeNavGraphSpec]
     */
    override val parentNavGraphSpec: ComposeNavGraphSpec

    /**
     * The [com.welu.composenavdestinations.navigation.destinations.ComposeDestination] this [ComposeDestinationSpec] is connected with.
     */
    val destination: D

}