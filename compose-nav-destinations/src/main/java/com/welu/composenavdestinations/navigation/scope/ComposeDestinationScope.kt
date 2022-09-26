package com.welu.composenavdestinations.navigation.scope

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.welu.composenavdestinations.navigation.destinations.ComposeDestination
import com.welu.composenavdestinations.navigation.spec.ComposeDestinationSpec

/**
 * Defines the main components inside a Destination content Block
 */
sealed interface ComposeDestinationScope {

    /**
     * The current NavBackStackEntry of the Destination
     */
    val backStackEntry: NavBackStackEntry

    /**
     * The android navigation NavController used for navigation inside the NavHost this [ComposeDestination] is contained in
     */
    val navController: NavHostController

    /**
     * Generic DestinationSpec which is connected to the Destination of this DestinationScope
     */
    val relatedSpec: ComposeDestinationSpec<*>

}