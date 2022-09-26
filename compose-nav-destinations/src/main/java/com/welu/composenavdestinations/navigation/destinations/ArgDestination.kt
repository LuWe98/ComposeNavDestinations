package com.welu.composenavdestinations.navigation.destinations

import com.welu.composenavdestinations.navigation.ArgDestinationCompositionScope
import com.welu.composenavdestinations.navigation.scope.ArgDestinationScope
import com.welu.composenavdestinations.navigation.transitions.NavComponentTransitions

interface ArgDestination<Arg : Any> : ComposeArgDestination<Arg, ArgDestinationScope<Arg>> {

    /**
     * The transitions this [ArgDestination] is associated with
     */
    val transitions: NavComponentTransitions get() = NavComponentTransitions.Default

    /**
     * Composables can be written inside of this typealias Scope Definition. It is easy to get hold of the parsed Arg of the Destination
     */
    override val Content: ArgDestinationCompositionScope<Arg>

}