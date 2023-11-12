package com.welu.composenavdestinations.navigation.transitions

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavBackStackEntry

typealias EnterTransitionProvider = AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?

typealias ExitTransitionProvider = AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?

interface NavComponentTransitions {
    val enterTransition: EnterTransitionProvider? get () = null
    val exitTransition: ExitTransitionProvider? get() = null
    val popEnterTransition: EnterTransitionProvider? get() = enterTransition
    val popExitTransition: ExitTransitionProvider? get() = exitTransition

    companion object Default: NavComponentTransitions
}