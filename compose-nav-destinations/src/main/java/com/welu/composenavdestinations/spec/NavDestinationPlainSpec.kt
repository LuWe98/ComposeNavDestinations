package com.welu.composenavdestinations.spec

interface NavDestinationPlainSpec: NavDestinationSpec {

    operator fun invoke(): Routable = Routable(route)

    override val route: String get() = baseRoute

}