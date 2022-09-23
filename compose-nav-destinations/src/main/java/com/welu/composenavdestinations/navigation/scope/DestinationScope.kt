package com.welu.composenavdestinations.navigation.scope

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.welu.composenavdestinations.navigation.destinations.Destination
import com.welu.composenavdestinations.navigation.spec.DestinationSpec

/**
 * Defines the main components inside a Destination content Block
 */
sealed interface DestinationScope {

    /**
     * The current NavBackStackEntry of the Destination
     */
    val backStackEntry: NavBackStackEntry

    /**
     * The android navigation NavController used for navigation inside the NavHost this [Destination] is contained in
     */
    val navController: NavController

    /**
     * Generic DestinationSpec which is connected to the Destination of this DestinationScope
     */
    val relatedSpec: DestinationSpec<*>

}