package com.welu.composenavdestinations.extensions.navigation

import androidx.navigation.NavBackStackEntry
import com.welu.composenavdestinations.navigation.destinations.ComposeDestination
import com.welu.composenavdestinations.service.ServiceLocator

@Suppress("UNCHECKED_CAST", "DEPRECATION")
internal operator fun <T> NavBackStackEntry.get(key: String): T? = arguments?.get(key) as T?

val NavBackStackEntry.navComponentSpec get() = ServiceLocator.composeDestinationService.findComponentSpec(destination.route!!)
val NavBackStackEntry.composeDestination get() = navComponentSpec as ComposeDestination<*>
