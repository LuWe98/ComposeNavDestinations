package com.welu.composenavdestinations.navigation.spec

/**
 * Marks a Class as a [NavigationComponent]. Such a class can be added with a NavGraphBuilder
 * and is identified with an unique [route]. [DestinationSpec] and [NavGraphSpec] implement this interface.
 */
sealed interface NavigationComponent {

    /**
     * This is the [route] without any argument definitions
     */
    val baseRoute: String

    /**
     * This is the complete [route] of this NavigationComponent with arguments
     */
    val route: String

}