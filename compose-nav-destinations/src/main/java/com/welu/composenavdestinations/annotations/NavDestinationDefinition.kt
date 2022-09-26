package com.welu.composenavdestinations.annotations

//TODO -> umbenennen in DestinationDefinition
@Target(AnnotationTarget.CLASS)
annotation class NavDestinationDefinition(
    val route: String = "",
    val deepLinks: Array<NavDeepLink> = []
)

/*
    //TODO -> Das noch einbauen - OLD: val type: KClass<out NavDestinationType> = NavDestinationType.Default::class,
    val destinationType: KClass<*> = Unit::class,
 */