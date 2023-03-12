package com.welu.composenavdestinations.service

import com.welu.composenavdestinations.navigation.destinations.ComposeDestination
import com.welu.composenavdestinations.navigation.spec.ComposeDestinationSpec
import com.welu.composenavdestinations.navigation.spec.ComposeNavGraphSpec
import com.welu.composenavdestinations.navigation.spec.NavComponentSpec
import kotlin.reflect.KClass

internal class ComposeDestinationService {

    private val navDestinationMap = mutableMapOf<KClass<out ComposeDestination<*>>, ComposeDestinationSpec<*>>()

    private val routeNavComponentMap = mutableMapOf<String, NavComponentSpec>()

    internal fun registerComposeNavGraphSpec(navGraphSpec: ComposeNavGraphSpec) {
        if(routeNavComponentMap.containsKey(navGraphSpec.route)) return

        routeNavComponentMap[navGraphSpec.route] = navGraphSpec

        navGraphSpec.childNavGraphSpecs.forEach {
            registerComposeNavGraphSpec(it)
        }

        navGraphSpec.childDestinationSpecs.forEach {
            routeNavComponentMap[it.route] = it
            navDestinationMap[it.destination::class] = it
        }
    }

    internal fun findDestinationSpec(destination: ComposeDestination<*>) = navDestinationMap[destination::class] ?: throw IllegalStateException(
        "Destination is not registered. Did you register the ComposeNavGraphSpec this ComposeDestination resides in?"
    )

    internal fun findComponentSpec(route: String) = routeNavComponentMap[route] ?: throw IllegalStateException(
        "Route of NavComponent is not registered. Did you register the ComposeNavGraphSpec this NavComponent resides in?"
    )
}