package com.welu.composenavdestinations.destinations.scope

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.welu.composenavdestinations.destinations.spec.DestinationSpec

/**
 * Defines the main components inside a Destination content Block
 */
sealed interface DestinationScope {

    /**
     * The current NavBackStackEntry of the Destination
     */
    val backStackEntry: NavBackStackEntry

    /**
     * The android navigation NavController used for navigation
     */
    val navController: NavController

    /**
     * Generic DestinationSpec which is connected to the Destination of this DestinationScope
     */
    val relatedSpec: DestinationSpec

}