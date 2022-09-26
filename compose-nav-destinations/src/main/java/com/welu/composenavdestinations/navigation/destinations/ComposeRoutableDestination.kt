package com.welu.composenavdestinations.navigation.destinations

import com.welu.composenavdestinations.navigation.scope.ComposeDestinationScope

sealed interface ComposeRoutableDestination<Scope: ComposeDestinationScope>: ComposeDestination<Scope>