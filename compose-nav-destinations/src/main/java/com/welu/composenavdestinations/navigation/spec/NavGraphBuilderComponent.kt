package com.welu.composenavdestinations.navigation.spec

/**
 * Marks a Class as a [NavGraphBuilderComponent]. Such a class can be added via NavGraphBuilder.composable or NavGraphBuilder.navigation
 * and is identified with an unique [route]. [DestinationSpec] and [NavGraphSpec] implement this interface.
 */
sealed interface NavGraphBuilderComponent {
    val route: String
}