package com.welu.composenavdestinations.spec

import androidx.navigation.NavDeepLink

sealed interface NavDestinationSpec {

    val route: String

    val baseRoute: String

    val deepLinks: List<NavDeepLink> get() = emptyList()

}