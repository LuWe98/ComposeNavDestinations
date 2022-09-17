package com.welu.composenavdestinations.navigation.spec

import com.welu.composenavdestinations.navigation.Routable

interface PlainNavGraphSpec: NavGraphSpec, Routable {

    override val route: String get() = baseRoute

    override val parameterizedRoute: String get() = baseRoute

}