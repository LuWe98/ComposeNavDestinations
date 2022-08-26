package com.welu.composenavdestinations.annotations

import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
annotation class NavDestination(
    val route: String = "",
    val navArgs: KClass<*> = Unit::class,
    val deepLinks: Array<NavDestinationDeepLink> = []
)