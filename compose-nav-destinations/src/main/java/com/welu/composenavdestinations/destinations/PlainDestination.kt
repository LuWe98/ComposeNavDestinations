package com.welu.composenavdestinations.destinations

import com.welu.composenavdestinations.destinations.scope.PlainCompositionScope
import com.welu.composenavdestinations.destinations.scope.PlainDestinationScope
import com.welu.composenavdestinations.destinations.utils.extractRouteName

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