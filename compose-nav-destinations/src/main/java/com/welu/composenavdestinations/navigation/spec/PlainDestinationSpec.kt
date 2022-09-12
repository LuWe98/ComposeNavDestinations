package com.welu.composenavdestinations.navigation.spec

import com.welu.composenavdestinations.navigation.Routable
import com.welu.composenavdestinations.navigation.destinations.PlainDestination

interface PlainDestinationSpec : DestinationSpec<PlainDestination>, Routable {

    override val route: String get() = baseRoute

    override val parameterizedRoute: String get() = baseRoute

}