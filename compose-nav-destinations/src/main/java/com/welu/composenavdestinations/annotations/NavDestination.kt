package com.welu.composenavdestinations.annotations

@Target(AnnotationTarget.CLASS)
annotation class NavDestination(
    val route: String = "",
    val deepLinks: Array<NavDestinationDeepLink> = []
)

annotation class NavDestinationDeepLink(
    val route: String
)