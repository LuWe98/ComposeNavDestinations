package com.welu.composenavdestinations.extensions.navigation

import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.welu.composenavdestinations.navigation.scope.ArgDestinationScope
import com.welu.composenavdestinations.navigation.scope.PlainDestinationScope
import com.welu.composenavdestinations.navigation.spec.*

@Suppress("UNCHECKED_CAST")
fun <N : NavGraphSpec> NavGraphBuilder.generateHierarchy(
    navGraphSpec: N,
    navController: NavHostController
): Unit = navGraphSpec.childNavComponentSpecs.forEach { navComponent ->
    when (navComponent) {
        is ArgDestinationSpec<*> -> createArgDestinationComposable(navComponent as ArgDestinationSpec<Any>, navController)
        is PlainDestinationSpec -> createPlainDestinationComposable(navComponent, navController)
        is NavGraphSpec -> createNavigationComposable(navComponent, navController)
    }
}

private fun NavGraphBuilder.createArgDestinationComposable(
    argDestinationSpec: ArgDestinationSpec<Any>,
    navController: NavHostController
) = composable(
    route = argDestinationSpec.route,
    arguments = argDestinationSpec.arguments,
    deepLinks = argDestinationSpec.deepLinks
) { backStackEntry ->
    val scope = remember {
        ArgDestinationScope(
            relatedSpec = argDestinationSpec,
            navController = navController,
            backStackEntry = backStackEntry
        )
    }
    argDestinationSpec.destination.Content(scope)
}

private fun NavGraphBuilder.createPlainDestinationComposable(
    plainDestinationSpec: PlainDestinationSpec,
    navController: NavHostController
) = composable(
    route = plainDestinationSpec.route,
    deepLinks = plainDestinationSpec.deepLinks
) { backStackEntry ->
    val scope = remember {
        PlainDestinationScope(
            relatedSpec = plainDestinationSpec,
            navController = navController,
            backStackEntry = backStackEntry
        )
    }
    plainDestinationSpec.destination.Content(scope)
}

private fun NavGraphBuilder.createNavigationComposable(
    navGraphSpec: NavGraphSpec,
    navController: NavHostController
) = navigation(
    route = navGraphSpec.route,
    startDestination = navGraphSpec.startComponentSpec.route,
    deepLinks = navGraphSpec.deepLinks,
    arguments = if(navGraphSpec is ArgDestinationSpec<*>) navGraphSpec.arguments else emptyList()
) {
    generateHierarchy(navGraphSpec, navController)
}



//fun <T> NavGraphBuilder.navDestination(
//    argsSpec: NavDestinationArgSpec<T>,
//    content: @Composable NavDestinationArgScope<T>.() -> Unit
//) = composable(
//    route = argsSpec.route,
//    arguments = argsSpec.arguments,
//    deepLinks = argsSpec.deepLinks,
//) {
//    content(NavDestinationArgScope(argsSpec, it))
//}
//
////Noch einbauen, dass man über einen type beim NavDestination bestimmen kann ob es ein dialog ist oder nicht
//fun <T> NavGraphBuilder.navDestinationDialog(
//    argsSpec: NavDestinationArgSpec<T>,
//    content: @Composable NavDestinationArgScope<T>.() -> Unit
//) = dialog(
//    route = argsSpec.route,
//    arguments = argsSpec.arguments,
//    deepLinks = argsSpec.deepLinks
//) {
//    content(NavDestinationArgScope(argsSpec, it))
//}
//
//fun NavGraphBuilder.navDestination(
//    plainSpec: NavDestinationPlainSpec,
//    content: @Composable NavDestinationPlainScope.() -> Unit
//) = composable(
//    route = plainSpec.route,
//    deepLinks = plainSpec.deepLinks
//) {
//    content(NavDestinationPlainScope(plainSpec, it))
//}