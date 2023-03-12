package com.welu.composenavdestinations.extensions.navigation

import androidx.navigation.NavBackStackEntry
import com.welu.composenavdestinations.navigation.destinations.ComposeDestination
import com.welu.composenavdestinations.navigation.spec.ComposeDestinationSpec
import com.welu.composenavdestinations.service.ServiceLocator

@Suppress("UNCHECKED_CAST", "DEPRECATION")
internal operator fun <T> NavBackStackEntry.get(key: String): T? = arguments?.get(key) as T?

val NavBackStackEntry.navComponentSpec get() = ServiceLocator.composeDestinationService.findComponentSpec(destination.route!!)

val NavBackStackEntry.composeDestinationSpec get() = navComponentSpec as ComposeDestinationSpec<*>

val NavBackStackEntry.composeDestination get() = composeDestinationSpec.destination