package com.welu.composenavdestinations.navigation.scope

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.welu.composenavdestinations.navigation.spec.DestinationSpec

/**
 * A PlainDestinationScope used for Destinations without Navargs
 */
data class DestinationScope(
    override val relatedSpec: DestinationSpec,
    override val navController: NavHostController,
    override val backStackEntry: NavBackStackEntry,
    val animatedVisibilityScope: AnimatedVisibilityScope
) : ComposeDestinationScope
