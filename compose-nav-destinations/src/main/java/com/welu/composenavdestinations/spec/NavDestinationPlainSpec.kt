package com.welu.composenavdestinations.spec

interface NavDestinationPlainSpec: NavDestinationSpec, NavDestinationRoute {

    operator fun invoke(): NavDestinationRoute = this

    override val route: String get() = baseRoute

    override val parameterizedRoute: String get() = baseRoute

}