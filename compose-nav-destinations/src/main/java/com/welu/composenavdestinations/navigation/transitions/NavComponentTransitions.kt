package com.welu.composenavdestinations.navigation.transitions

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavBackStackEntry

@OptIn(ExperimentalAnimationApi::class)
typealias EnterTransitionProvider = AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?

@OptIn(ExperimentalAnimationApi::class)
typealias ExitTransitionProvider = AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?

@OptIn(ExperimentalAnimationApi::class)
interface NavComponentTransitions {

    val enterTransition: EnterTransitionProvider? get () = null

    val exitTransition: ExitTransitionProvider? get() = null

    val popEnterTransition: EnterTransitionProvider? get() = enterTransition

    val popExitTransition: ExitTransitionProvider? get() = exitTransition

    companion object Default: NavComponentTransitions

}