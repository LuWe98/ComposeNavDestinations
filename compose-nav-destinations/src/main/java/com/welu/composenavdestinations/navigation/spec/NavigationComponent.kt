package com.welu.composenavdestinations.navigation.spec

/**
 * Marks a Class as a [NavigationComponent]. Such a class can be added with a NavGraphBuilder
 * and is identified with an unique [route]. [DestinationSpec] and [NavGraphSpec] implement this interface.
 */
sealed interface NavigationComponent {
    val route: String
}