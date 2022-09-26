package com.welu.composenavdestinations.navigation.spec

import com.welu.composenavdestinations.navigation.Routable

interface NavGraphSpec: ComposeNavGraphSpec, Routable {

    override val route: String get() = baseRoute

    override val parameterizedRoute: String get() = baseRoute

}