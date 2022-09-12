package com.welu.composenavdestinations.annotations

import kotlin.reflect.KClass

@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class NavGraphDefinition(
    val route: String = "",
    val isDefaultNavGraph: Boolean = false,
    val argsClass: KClass<*> = Unit::class,
    val deepLinks: Array<NavDeepLink> = []
)