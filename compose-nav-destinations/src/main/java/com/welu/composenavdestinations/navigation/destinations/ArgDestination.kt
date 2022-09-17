package com.welu.composenavdestinations.navigation.destinations

import com.welu.composenavdestinations.navigation.ArgCompositionScope
import com.welu.composenavdestinations.navigation.scope.ArgDestinationScope

//TODO Wegen der Route Testen:
// Wenn route angegeben wird extension function erstellt mit dem gleichen namen, die dann stattdessen aufgerufen wird

interface ArgDestination<Arg : Any> : Destination<ArgDestinationScope<Arg>> {

    /**
     * Composables can be written inside of this typealias Scope Definition. It is easy to get hold of the parsed Arg of the Destination
     */
    override val Content: ArgCompositionScope<Arg>

}