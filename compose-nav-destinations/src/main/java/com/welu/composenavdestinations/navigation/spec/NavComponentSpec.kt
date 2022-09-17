package com.welu.composenavdestinations.navigation.spec

import androidx.navigation.NavDeepLink

/**
 * Marks a Class as a [NavComponentSpec]. Such a class can be added with a NavGraphBuilder
 * and is identified with an unique [route]. [DestinationSpec] and [NavGraphSpec] implement this interface.
 */
sealed interface NavComponentSpec {

    /**
     * This is the [route] without any argument definitions
     */
    val baseRoute: String

    /**
     * This is the complete [route] of this [NavComponentSpec] with arguments
     */
    val route: String

    /**
     * Contains the [NavDeepLink] to this [NavComponentSpec]
     */
    val deepLinks: List<NavDeepLink> get() = emptyList()

    /**
     * This is the parent [NavGraphSpec] of this [NavComponentSpec]. Can be null when this [NavComponentSpec] is a [NavGraphSpec] and is in the root of the NavHost.
     */
    val parentNavGraphSpec: NavGraphSpec?

}