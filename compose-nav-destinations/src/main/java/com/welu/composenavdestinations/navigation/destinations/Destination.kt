package com.welu.composenavdestinations.navigation.destinations

import com.welu.composenavdestinations.navigation.DestinationCompositionScope
import com.welu.composenavdestinations.navigation.scope.DestinationScope
import com.welu.composenavdestinations.navigation.transitions.NavComponentTransitions

interface Destination: ComposeRoutableDestination<DestinationScope> {

    /**
     * The transitions this [Destination] is associated with
     */
    val transitions: NavComponentTransitions get() = NavComponentTransitions.Default

    /**
     * Composables can be written inside of this typealias Scope Definition
     */
    override val Content: DestinationCompositionScope

}