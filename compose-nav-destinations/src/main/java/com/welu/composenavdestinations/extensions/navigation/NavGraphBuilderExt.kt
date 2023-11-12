package com.welu.composenavdestinations.extensions.navigation

import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet
import com.welu.composenavdestinations.navigation.scope.*
import com.welu.composenavdestinations.navigation.spec.*

fun <N : ComposeNavGraphSpec> NavGraphBuilder.addNavGraph(
    navGraphSpec: N,
    navController: NavHostController
) {
    addNavGraphInternal(
        navGraphSpec = navGraphSpec,
        navController = navController
    )
}

private fun <N : ComposeNavGraphSpec> NavGraphBuilder.addNavGraphInternal(
    navGraphSpec: N,
    navController: NavHostController
) {
    @Suppress("UNCHECKED_CAST")
    navGraphSpec.childNavComponentSpecs.forEach { navComponent ->
        when (navComponent) {
            is DestinationSpec -> addDestinationComposable(navComponent, navController)
            is ArgDestinationSpec<*> -> addArgDestinationComposable(navComponent as ArgDestinationSpec<Any>, navController)
            is DialogDestinationSpec -> addDialogDestination(navComponent, navController)
            is DialogArgDestinationSpec<*> -> addDialogArgDestination(navComponent as DialogArgDestinationSpec<Any>, navController)
            is BottomSheetDestinationSpec -> addBottomSheetDestination(navComponent, navController)
            is BottomSheetArgDestinationSpec<*> -> addBottomSheetArgDestination(navComponent as BottomSheetArgDestinationSpec<Any>, navController)
            is ComposeNavGraphSpec -> addNestedNavigation(navComponent, navController)
        }
    }
}

//TODO -> Das vllt noch oben einbauen?
private fun NavGraphBuilder.addNestedNavigation(
    navGraphSpec: ComposeNavGraphSpec,
    navController: NavHostController
) {
    navigation(
        route = navGraphSpec.route,
        startDestination = navGraphSpec.startComponentSpec.route,
        deepLinks = navGraphSpec.deepLinks,
        arguments = when (navGraphSpec) {
            is ArgNavGraphSpec<*> -> navGraphSpec.arguments
            is NavGraphSpec -> emptyList()
        }
    ) {
        addNavGraphInternal(navGraphSpec, navController)
    }
}

private fun NavGraphBuilder.addDestinationComposable(
    destinationSpec: DestinationSpec,
    navController: NavHostController
) {
    composable(
        route = destinationSpec.route,
        deepLinks = destinationSpec.deepLinks,
        enterTransition = destinationSpec.destination.transitions.enterTransition,
        exitTransition = destinationSpec.destination.transitions.exitTransition,
        popEnterTransition = destinationSpec.destination.transitions.popEnterTransition,
        popExitTransition = destinationSpec.destination.transitions.popExitTransition
    ) { backStackEntry ->
        val scope = remember {
            DestinationScope(
                relatedSpec = destinationSpec,
                navController = navController,
                backStackEntry = backStackEntry,
                animatedVisibilityScope = this
            )
        }

        destinationSpec.destination.Content(scope)
    }
}

private fun NavGraphBuilder.addArgDestinationComposable(
    argDestinationSpec: ArgDestinationSpec<Any>,
    navController: NavHostController
) {
    composable(
        route = argDestinationSpec.route,
        arguments = argDestinationSpec.arguments,
        deepLinks = argDestinationSpec.deepLinks,
        enterTransition = argDestinationSpec.destination.transitions.enterTransition,
        exitTransition = argDestinationSpec.destination.transitions.exitTransition,
        popEnterTransition = argDestinationSpec.destination.transitions.popEnterTransition,
        popExitTransition = argDestinationSpec.destination.transitions.popExitTransition
    ) { backStackEntry ->
        val scope = remember {
            ArgDestinationScope(
                relatedSpec = argDestinationSpec,
                navController = navController,
                backStackEntry = backStackEntry,
                animatedVisibilityScope = this
            )
        }
        argDestinationSpec.destination.Content(scope)
    }
}

private fun NavGraphBuilder.addDialogDestination(
    dialogDestinationSpec: DialogDestinationSpec,
    navController: NavHostController
) {
    dialog(
        route = dialogDestinationSpec.route,
        deepLinks = dialogDestinationSpec.deepLinks,
        dialogProperties = dialogDestinationSpec.destination.properties
    ) { backStackEntry ->
        val scope = remember {
            DialogDestinationScope(
                relatedSpec = dialogDestinationSpec,
                navController = navController,
                backStackEntry = backStackEntry
            )
        }
        dialogDestinationSpec.destination.Content(scope)
    }
}

private fun NavGraphBuilder.addDialogArgDestination(
    dialogArgDestinationSpec: DialogArgDestinationSpec<Any>,
    navController: NavHostController
) {
    dialog(
        route = dialogArgDestinationSpec.route,
        arguments = dialogArgDestinationSpec.arguments,
        deepLinks = dialogArgDestinationSpec.deepLinks,
        dialogProperties = dialogArgDestinationSpec.destination.properties
    ) { backStackEntry ->
        val scope = remember {
            DialogArgDestinationScope(
                relatedSpec = dialogArgDestinationSpec,
                navController = navController,
                backStackEntry = backStackEntry
            )
        }
        dialogArgDestinationSpec.destination.Content(scope)
    }
}

@OptIn(ExperimentalMaterialNavigationApi::class)
private fun NavGraphBuilder.addBottomSheetDestination(
    bottomSheetDestinationSpec: BottomSheetDestinationSpec,
    navController: NavHostController
) {
    bottomSheet(
        route = bottomSheetDestinationSpec.route,
        deepLinks = bottomSheetDestinationSpec.deepLinks,
    ) { backStackEntry ->
        val scope = remember {
            BottomSheetDestinationScope(
                relatedSpec = bottomSheetDestinationSpec,
                navController = navController,
                backStackEntry = backStackEntry,
                columnScope = this
            )
        }
        bottomSheetDestinationSpec.destination.Content(scope)
    }
}

@OptIn(ExperimentalMaterialNavigationApi::class)
private fun NavGraphBuilder.addBottomSheetArgDestination(
    bottomSheetArgDestinationSpec: BottomSheetArgDestinationSpec<Any>,
    navController: NavHostController
) {
    bottomSheet(
        route = bottomSheetArgDestinationSpec.route,
        arguments = bottomSheetArgDestinationSpec.arguments,
        deepLinks = bottomSheetArgDestinationSpec.deepLinks,
    ) { backStackEntry ->
        val scope = remember {
            BottomSheetArgDestinationScope(
                relatedSpec = bottomSheetArgDestinationSpec,
                navController = navController,
                backStackEntry = backStackEntry,
                columnScope = this
            )
        }
        bottomSheetArgDestinationSpec.destination.Content(scope)
    }
}