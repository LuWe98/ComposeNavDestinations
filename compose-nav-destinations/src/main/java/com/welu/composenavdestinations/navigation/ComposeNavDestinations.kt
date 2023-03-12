package com.welu.composenavdestinations.navigation

import com.welu.composenavdestinations.navigation.spec.ComposeNavGraphSpec
import com.welu.composenavdestinations.service.ServiceLocator

object ComposeNavDestinations {
    fun init(navGraphs: List<ComposeNavGraphSpec>) {
        ServiceLocator.composeDestinationService.let {
            navGraphs.forEach(it::registerComposeNavGraphSpec)
        }
    }
}