package com.welu.composenavdestinations.spec

import androidx.navigation.NavDeepLink

sealed interface NavDestinationSpec: Route {

    val baseRoute: String

    val deepLinks: List<NavDeepLink> get() = emptyList()

}