package com.welu.composenavdestinations.annotations

import com.welu.composenavdestinations.navigation.transitions.NavComponentTransitions
import kotlin.reflect.KClass

@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class ComposeNavGraph constructor(
    val route: String = "",
    val isDefaultNavGraph: Boolean = false,
    val argsClass: KClass<*> = Unit::class,
    val transitionClass: KClass<out NavComponentTransitions> = NavComponentTransitions.Default::class,
    val deepLinks: Array<NavComponentDeepLink> = []
)