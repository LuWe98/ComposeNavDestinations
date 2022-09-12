package com.welu.composenavdestinations.annotations

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
annotation class NavDestinationDefinition(
    val route: String = "",
    //TODO -> Das noch einbauen
    val destinationType: KClass<*> = Unit::class,
    val deepLinks: Array<NavDeepLink> = []
)