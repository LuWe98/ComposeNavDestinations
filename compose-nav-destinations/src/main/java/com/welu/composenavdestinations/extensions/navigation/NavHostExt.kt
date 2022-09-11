package com.welu.composenavdestinations.extensions.navigation

//
//@Composable
//fun NavDestinationNavHost(
//    startDestination: NavDestinationPlainSpec,
//    navController: NavHostController,
//    modifier: Modifier = Modifier,
//    route: String? = null,
//    builder: NavGraphBuilder.() -> Unit
//) = NavHost(
//    navController = navController,
//    startDestination = startDestination.baseRoute,
//    modifier = modifier,
//    route = route
//) {
//    builder()
//}
//
//
//
//
//
//
//@Composable
//fun NavDestinationNavHost2(
//    startDestination: NavDestinationPlainSpec,
//    navController: NavHostController,
//    modifier: Modifier = Modifier,
//    route: String? = null,
//    builder: NavDestinationNavHostBuilder.() -> Unit
//) = NavHost(
//    navController = navController,
//    startDestination = startDestination.baseRoute,
//    modifier = modifier,
//    route = route
//) {
//    builder(NavDestinationNavHostBuilder(this, navController))
//}
//
//
//
//
//class NavDestinationNavHostBuilder(
//    val navGraphBuilder: NavGraphBuilder,
//    val navController: NavHostController
//)
//
//fun <T> NavDestinationNavHostBuilder.navDestination(
//    argsSpec: NavDestinationArgSpec<T>,
//    content: @Composable NavDestinationArgScope<T>.() -> Unit
//) = navGraphBuilder.composable(
//    route = argsSpec.route,
//    arguments = argsSpec.arguments,
//    deepLinks = argsSpec.deepLinks,
//) {
//    content(NavDestinationArgScope(argsSpec, it))
//}
//
////Noch einbauen, dass man über einen type beim NavDestination bestimmen kann ob es ein dialog ist oder nicht
//fun <T> NavDestinationNavHostBuilder.navDestinationDialog(
//    argsSpec: NavDestinationArgSpec<T>,
//    content: @Composable NavDestinationArgScope<T>.() -> Unit
//) = navGraphBuilder.dialog(
//    route = argsSpec.route,
//    arguments = argsSpec.arguments,
//    deepLinks = argsSpec.deepLinks
//) {
//    content(NavDestinationArgScope(argsSpec, it))
//}
//
//fun NavDestinationNavHostBuilder.navDestination(
//    plainSpec: NavDestinationPlainSpec,
//    content: @Composable NavDestinationPlainScope.() -> Unit
//) = navGraphBuilder.composable(
//    route = plainSpec.route,
//    deepLinks = plainSpec.deepLinks
//) {
//    content(NavDestinationPlainScope(plainSpec, it))
//}
//
//inline fun <reified T> NavDestinationNavHostBuilder.sendDestinationResult(
//    toSpec: NavDestinationSpec,
//    value: T?,
//    keySpecification: String? = null
//) = navController.sendDestinationResult(toSpec, value, keySpecification)
//
//@Composable
//@SuppressLint("ComposableNaming")
//inline fun <reified T> NavDestinationNavHostBuilder.destinationResultListener(
//    forSpec: NavDestinationSpec,
//    keySpecification: String? = null,
//    withLifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
//    crossinline callback: (T) -> Unit
//) = navController.destinationResultListener(
//    forSpec = forSpec,
//    keySpecification = keySpecification,
//    withLifecycleState = withLifecycleState,
//    callback = callback
//)