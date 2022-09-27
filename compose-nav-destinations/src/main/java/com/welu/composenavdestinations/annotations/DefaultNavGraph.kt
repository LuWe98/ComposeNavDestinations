package com.welu.composenavdestinations.annotations

import com.welu.composenavdestinations.navigation.spec.ComposeNavGraphSpec

/**
 * This is the default [ComposeNavGraph].
 *
 * NavComponents will be added to the generated [ComposeNavGraphSpec] of this [DefaultNavGraph] when no other default [ComposeNavGraph] is defined.
 */
@ComposeNavGraph(isDefaultNavGraph = true)
annotation class DefaultNavGraph(
    val isStart: Boolean = false
)