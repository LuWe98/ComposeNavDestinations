package com.welu.compose_nav_destinations_ksp.generation.templates

import com.welu.compose_nav_destinations_ksp.utils.PackageUtils.NAV_COMPONENT_UTILS_FILE_IMPORT

internal object CodeTemplates {

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Template for NavComponentUtils File

    const val PLACEHOLDER_NAV_UTILS_ALL_COMPONENT_SPECS_SET = "NAV_UTILS_ALL_COMPONENT_SPECS_SET"
    const val PLACEHOLDER_NAV_UTILS_DESTINATION_SPEC_MAP = "NAV_UTILS_DESTINATION_SPEC_MAP_PLACEHOLDER"

    val NAV_COMPONENT_UTILS_TEMPLATE =
    """
    | object ${NAV_COMPONENT_UTILS_FILE_IMPORT.simpleName} {
    |     
    |     val allNavComponentSpecs: Set<NavComponentSpec> = setOf(
    |         $PLACEHOLDER_NAV_UTILS_ALL_COMPONENT_SPECS_SET
    |     )
    |     
    |     val allComposeDestinationSpecs: List<ComposeDestinationSpec<*>> = allNavComponentSpecs.filterIsInstance<ComposeDestinationSpec<*>>()
    |     
    |     val allComposeNavGraphSpecs: List<ComposeNavGraphSpec> = allNavComponentSpecs.filterIsInstance<ComposeNavGraphSpec>()
    |     
    |     val destinationSpecMap: Map<ComposeDestination<*>, ComposeDestinationSpec<*>> = mapOf(
    |         $PLACEHOLDER_NAV_UTILS_DESTINATION_SPEC_MAP
    |     )
    |     
    |     fun findSpecWith(route: String): NavComponentSpec? = allNavComponentSpecs.firstOrNull { it.route == route }
    |         
    |     fun findDestinationWith(route: String): ComposeDestination<*>? = destinationSpecMap.keys.firstOrNull { it.route == route }
    |    
    | }
    """.trimMargin("| ")



    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Template for Destination Extension File

    private val DESTINATION_FIND_SPEC_EXTENSION_FUNCTIONS =
    """
    | fun ComposeDestination<*>.findSpec(): ComposeDestinationSpec<*> = ${NAV_COMPONENT_UTILS_FILE_IMPORT.simpleName}.destinationSpecMap[this] 
    |     ?: throw IllegalArgumentException("Destination is not annotated with NavDestinationDefinition")
    | 
    | @Suppress("UNCHECKED_CAST")
    | fun <Arg : Any> ComposeArgDestination<Arg, *>.getSpec(): ComposeArgDestinationSpec<Arg, *> = findSpec() as ComposeArgDestinationSpec<Arg, *>
    | 
    | fun ComposeRoutableDestination<*>.getSpec(): ComposeRoutableDestinationSpec<*> = findSpec() as ComposeRoutableDestinationSpec<*>
    | 
    | val ComposeDestination<*>.route get() = findSpec().route
    | 
    """.trimMargin("| ")


    //TODO -> Extension Files
    const val PLACEHOLDER_DESTINATION_EXT_ARGS_FROM_METHODS = "DESTINATION_EXT_ARGS_FROM_FUNCTIONS"
    const val PLACEHOLDER_DESTINATION_EXT_ARGS_INVOKE_FUNCTION = "DESTINATION_EXT_ARGS_INVOKE_FUNCTION"

    val DESTINATION_EXT_TEMPLATE =
    """    
    | $PLACEHOLDER_DESTINATION_EXT_ARGS_FROM_METHODS
    | 
    | $PLACEHOLDER_DESTINATION_EXT_ARGS_INVOKE_FUNCTION
    | 
    | $DESTINATION_FIND_SPEC_EXTENSION_FUNCTIONS
    """.trimMargin("| ")



    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Templates for NavController Extension File

    private val NAV_CONTROLLER_NAVIGATE_WITH_ROUTABLE_DESTINATION =
    """
    | fun NavController.navigate(
    |   toDestination: ComposeRoutableDestination<*>
    | ) = navigate(toDestination.route)
    | 
    | fun ComposeDestinationScope.navigate(
    |   toDestination: ComposeRoutableDestination<*>
    | ) = navController.navigate(toDestination)
    | 
    | fun NavController.navigate(
    |   toDestination: ComposeRoutableDestination<*>,
    |   builder: NavOptionsBuilder.() -> Unit
    | ) = navigate(toDestination.route, builder)
    | 
    | fun ComposeDestinationScope.navigate(
    |   toDestination: ComposeRoutableDestination<*>,
    |   builder: NavOptionsBuilder.() -> Unit
    | ) = navController.navigate(toDestination, builder)
    | 
    | fun NavController.navigate(
    |    toDestination: ComposeRoutableDestination<*>,
    |    navOptions: NavOptions? = null,
    |    navigatorExtras: Navigator.Extras? = null
    | ) = navigate(toDestination.route, navOptions, navigatorExtras)
    | 
    | fun ComposeDestinationScope.navigate(
    |    toDestination: ComposeRoutableDestination<*>,
    |    navOptions: NavOptions? = null,
    |    navigatorExtras: Navigator.Extras? = null
    | ) = navController.navigate(toDestination, navOptions, navigatorExtras)
    | 
    | fun NavController.navigateAndPopUpTo(
    |    toDestination: ComposeRoutableDestination<*>,
    |    popUpTo: String,
    |    inclusive: Boolean = true
    | ) {
    |    val extras = NavOptions.Builder().setPopUpTo(popUpTo, inclusive).build()
    |    navigate(toDestination.route, extras)
    | }
    | 
    | fun ComposeDestinationScope.navigateAndPopUpTo(
    |    toDestination: ComposeRoutableDestination<*>,
    |    popUpTo: String,
    |    inclusive: Boolean = true
    | ) = navController.navigateAndPopUpTo(toDestination, popUpTo, inclusive)
    | 
    | fun NavController.navigateAndPopUpTo(
    |    toDestination: ComposeRoutableDestination<*>,
    |    popUpToSpec: ComposeDestinationSpec<*>,
    |    inclusive: Boolean = true
    | ) = navigateAndPopUpTo(toDestination, popUpToSpec.route, inclusive)
    | 
    | fun ComposeDestinationScope.navigateAndPopUpTo(
    |    toDestination: ComposeRoutableDestination<*>,
    |    popUpToSpec: ComposeDestinationSpec<*>,
    |    inclusive: Boolean = true
    | ) = navController.navigateAndPopUpTo(toDestination, popUpToSpec, inclusive)
    | 
    | fun NavController.navigateAndPopUpTo(
    |    toDestination: ComposeRoutableDestination<*>,
    |    popUpToDestination: ComposeDestination<*>,
    |    inclusive: Boolean = true
    | ) = navigateAndPopUpTo(toDestination, popUpToDestination.route, inclusive)
    | 
    | fun ComposeDestinationScope.navigateAndPopUpTo(
    |    toDestination: ComposeRoutableDestination<*>,
    |    popUpToDestination: ComposeDestination<*>,
    |    inclusive: Boolean = true
    | ) = navController.navigateAndPopUpTo(toDestination, popUpToDestination.route, inclusive)
    | 
    | fun NavController.navigateAndPopUpTo(
    |    toRoutable: Routable,
    |    popUpTo: ComposeDestination<*>,
    |    inclusive: Boolean = true
    | ) {
    |    val extras = NavOptions.Builder().setPopUpTo(popUpTo.route, inclusive).build()
    |    navigate(toRoutable.parameterizedRoute, extras)
    | }
    | 
    | fun ComposeDestinationScope.navigateAndPopUpTo(
    |    toRoutable: Routable,
    |    popUpTo: ComposeDestination<*>,
    |    inclusive: Boolean = true
    | ) = navController.navigateAndPopUpTo(toRoutable, popUpTo, inclusive)
    | 
    | fun NavController.popBackStack(
    |    toDestination: ComposeDestination<*>,
    |    inclusive: Boolean = false,
    |    saveState: Boolean = false
    | ) = popBackStack(toDestination.route, inclusive, saveState)
    | 
    | fun ComposeDestinationScope.popBackStack(
    |    toDestination: ComposeDestination<*>,
    |    inclusive: Boolean = false,
    |    saveState: Boolean = false
    | ) = navController.popBackStack(toDestination, inclusive, saveState)
    | 
    """.trimMargin("| ")


    val NAV_CONTROLLER_EXTENSIONS_TEMPLATE =
    """
    | $NAV_CONTROLLER_NAVIGATE_WITH_ROUTABLE_DESTINATION    
    """.trimMargin("| ")


    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Templates for NavBackStackEntry Extension File

    val NAV_BACK_STACK_ENTRY_EXTENSIONS_TEMPLATE =
    """
    | val NavBackStackEntry.navComponentSpec get() = NavComponentUtils.allNavComponentSpecs.firstOrNull { it.route == destination.route }
    | 
    | val NavBackStackEntry.composeDestination get() = NavComponentUtils.allComposeDestinationSpecs.firstOrNull { it.route == destination.route }?.destination
    """.trimMargin("| ")



    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Template for Result Extensions File

    private val DESTINATION_SCOPE_SEND_RESULT_TO_DESTINATION_EXTENSION_FUNCTION =
    """
    | inline fun <reified T> ComposeDestinationScope.sendDestinationResultTo(
    |     destination: ComposeDestination<*>,
    |     value: T?,
    |     keySpecification: String? = null
    | ): Unit = navController.sendDestinationResultTo(
    |     spec = destination.findSpec(), 
    |     value = value, 
    |     keySpecification = keySpecification
    | )  
    """.trimMargin("| ")

    private val NAV_CONTROLLER_SEND_RESULT_TO_DESTINATION_EXTENSION_FUNCTION =
    """
    | inline fun <reified T> NavController.sendDestinationResultTo(
    |     destination: ComposeDestination<*>,
    |     value: T?,
    |     keySpecification: String? = null
    | ): Unit = sendDestinationResultTo(
    |     spec = destination.findSpec(), 
    |     value = value, 
    |     keySpecification = keySpecification
    | )  
    """.trimMargin("| ")

    private val NAV_CONTROLLER_FLOW_RESULT_LISTENER_WITH_DESTINATION =
    """
    | @Composable
    | inline fun <reified T> NavController.DestinationResultListener(
    |     forDestination: ComposeDestination<*>,
    |     keySpecification: String? = null,
    |     withLifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    |     crossinline callback: (T) -> Unit
    | ) = DestinationResultListener(
    |     forSpec = forDestination.findSpec(),
    |     keySpecification = keySpecification,
    |     withLifecycleState = withLifecycleState,
    |     callback = callback
    | )
    """.trimMargin("| ")

    private val NAV_CONTROLLER_LIFECYCLE_RESULT_LISTENER_WITH_DESTINATION =
    """
    | @Composable
    | inline fun <reified T> NavController.LifecycleDestinationResultListener(
    |     forDestination: ComposeDestination<*>,
    |     keySpecification: String? = null,
    |     crossinline callback: (T) -> Unit
    | ) = LifecycleDestinationResultListener(
    |     forSpec = forDestination.findSpec(),
    |     keySpecification = keySpecification,
    |     callback = callback
    | )
    """.trimMargin("| ")

    val NAV_DESTINATION_RESULT_EXTENSIONS_TEMPLATE =
    """
    | $DESTINATION_SCOPE_SEND_RESULT_TO_DESTINATION_EXTENSION_FUNCTION
    | 
    | $NAV_CONTROLLER_SEND_RESULT_TO_DESTINATION_EXTENSION_FUNCTION
    | 
    | $NAV_CONTROLLER_FLOW_RESULT_LISTENER_WITH_DESTINATION
    | 
    | $NAV_CONTROLLER_LIFECYCLE_RESULT_LISTENER_WITH_DESTINATION
    """.trimMargin("| ")

}