package com.welu.composenavdestinations.annotations

import kotlin.reflect.KClass

//TODO -> Vllt umbenennen in NavDestination
@Target(AnnotationTarget.CLASS)
annotation class NavDestinationDefinition(
    val route: String = "",
    //TODO -> Das noch einbauen - OLD: val type: KClass<out NavDestinationType> = NavDestinationType.Default::class,
    val destinationType: KClass<*> = Unit::class,
    val deepLinks: Array<NavDeepLink> = []
)