package com.welu.composenavdestinations.annotations

@Target(AnnotationTarget.CLASS)
annotation class NavDestinationDefinition(
    val route: String = "",
    val destinationType: String = "",
    val deepLinks: Array<String> = []
)
