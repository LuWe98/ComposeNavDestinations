package com.welu.composenavdestinations.destinations.spec

import com.welu.composenavdestinations.destinations.Routable

interface PlainDestinationSpec : DestinationSpec, Routable {

    override val route: String get() = baseRoute

    override val parameterizedRoute: String get() = baseRoute

}