package com.welu.composenavdestinations.navigation.destinations

import com.welu.composenavdestinations.navigation.Routable
import com.welu.composenavdestinations.navigation.PlainCompositionScope
import com.welu.composenavdestinations.navigation.scope.PlainDestinationScope
import com.welu.composenavdestinations.navigation.utils.extractRouteName

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