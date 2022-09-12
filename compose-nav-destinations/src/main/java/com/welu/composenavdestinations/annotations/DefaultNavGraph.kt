package com.welu.composenavdestinations.annotations

/**
 * This is the default [NavGraphDefinition].
 *
 * Destinations will be added to the generated [com.welu.composenavdestinations.navigation.spec.NavGraphSpec] of this [DefaultNavGraph] when no other default [NavGraphDefinition] is defined.
 */
@NavGraphDefinition(isDefaultNavGraph = true)
annotation class DefaultNavGraph(
    val isStart: Boolean = false
)