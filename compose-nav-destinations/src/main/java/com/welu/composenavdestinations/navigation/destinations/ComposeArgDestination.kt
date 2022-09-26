package com.welu.composenavdestinations.navigation.destinations

import com.welu.composenavdestinations.navigation.scope.ComposeArgDestinationScope

interface ComposeArgDestination<Arg: Any, Scope: ComposeArgDestinationScope<Arg>>: ComposeDestination<Scope>