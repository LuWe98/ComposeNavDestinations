package com.welu.composenavdestinations.annotations

import androidx.compose.animation.ExperimentalAnimationApi
import com.welu.composenavdestinations.navigation.transitions.NavComponentTransitions
import kotlin.reflect.KClass

@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class NavGraphDefinition constructor(
    val route: String = "",
    val isDefaultNavGraph: Boolean = false,
    val argsClass: KClass<*> = Unit::class,
    @OptIn(ExperimentalAnimationApi::class)
    val transitionClass: KClass<out NavComponentTransitions> = NavComponentTransitions.Default::class,
    val deepLinks: Array<NavDeepLink> = []
)