package com.welu.composenavdestinations.navigation.spec

import com.welu.composenavdestinations.navigation.destinations.ArgDestination

//TODO -> Beide Wege (argsFrom) sind für eine Destination eigentlich in Ordnung, da die Argumente immer vorliegen sollten
// -> Falsch, man kann zu einem Graphen navigieren, welcher automatisch zu startDestination weiterleitet
// Für NavArgSpecs ist es jedoch nicht in Ordnung, da man direkt zu einer Destination im Graphen navigieren kann und somit keine Args an den NavGraph übergibt.

/**
 * Describes a Destination with defines NavArgs. These can be easily obtained trough the [argsFrom] methods.
 */
interface ArgDestinationSpec<Arg : Any> : ComposeArgDestinationSpec<Arg, ArgDestination<Arg>>