package com.welu.composenavdestinations.extensions.navigation

import com.welu.composenavdestinations.navigation.destinations.ComposeDestination
import com.welu.composenavdestinations.service.ServiceLocator

val ComposeDestination<*>.spec get() = ServiceLocator.composeDestinationService.findDestinationSpec(this)

val ComposeDestination<*>.route get() = spec.route
