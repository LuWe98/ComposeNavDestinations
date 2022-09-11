package com.welu.composenavdestinations.destinations

import com.welu.composenavdestinations.destinations.scope.ArgCompositionScope
import com.welu.composenavdestinations.destinations.scope.ArgDestinationScope

//TODO Wegen der Route Testen:
// Wenn route angegeben wird extension function erstellt mit dem gleichen namen, die dann stattdessen aufgerufen wird

interface ArgDestination<Arg : Any> : Destination<ArgDestinationScope<Arg>> {

    /**
     * Composables can be written inside of this typealias Scope Definition. It is easy to get hold of the parsed Arg of the Destination
     */
    override val content: ArgCompositionScope<Arg>

}