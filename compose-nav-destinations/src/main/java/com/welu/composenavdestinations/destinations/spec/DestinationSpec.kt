package com.welu.composenavdestinations.destinations.spec

import androidx.navigation.NavDeepLink

//TODO -> Noch einen NavGraph einfügen als Variable
/**
 * Defines generated arguments of an Destination
 */
sealed interface DestinationSpec {

    val route: String

    val baseRoute: String

    val deepLinks: List<NavDeepLink> get() = emptyList()

}