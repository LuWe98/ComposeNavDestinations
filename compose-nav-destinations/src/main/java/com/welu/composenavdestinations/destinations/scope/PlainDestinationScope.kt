package com.welu.composenavdestinations.destinations.scope

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.welu.composenavdestinations.destinations.spec.PlainDestinationSpec

/**
 * A PlainDestinationScope used for Destinations without Navargs
 */
data class PlainDestinationScope(
    override val relatedSpec: PlainDestinationSpec,
    override val navController: NavController,
    override val backStackEntry: NavBackStackEntry
) : DestinationScope
