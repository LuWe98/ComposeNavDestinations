package com.welu.composenavdestinations.navigation.spec

import com.welu.composenavdestinations.navigation.Routable
import com.welu.composenavdestinations.navigation.destinations.ComposeRoutableDestination

sealed interface ComposeRoutableDestinationSpec<D: ComposeRoutableDestination<*>> : ComposeDestinationSpec<D>, Routable {

    override val route: String get() = baseRoute

    override val parameterizedRoute: String get() = baseRoute

}