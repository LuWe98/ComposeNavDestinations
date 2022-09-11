package com.welu.composenavdestinations.generation

import com.welu.composenavdestinations.utils.PackageUtils
import com.welu.composenavdestinations.utils.PackageUtils.NAV_DESTINATION_UTILS_FILE_IMPORT

internal object CodeTemplates {

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Template for DestinationPlainSpec


    const val PLACEHOLDER_NAV_SPEC_BASE_ROUTE = "BASE_ROUTE_PLACEHOLDER"
    const val PLACEHOLDER_NAV_SPEC_DESTINATION_NAME = "DESTINATION_NAME_PLACEHOLDER"
    const val PLACEHOLDER_NAV_SPEC_DEEPLINK_VALUE = "DEEPLINK_VALUE_PLACEHOLDER"

    val NAV_DESTINATION_PLAIN_SPEC_TEMPLATE =
    """
    | object $PLACEHOLDER_NAV_SPEC_DESTINATION_NAME: ${PackageUtils.NAV_DESTINATION_PLAIN_SPEC_IMPORT.simpleName} {
    | 
    |     override val baseRoute = "$PLACEHOLDER_NAV_SPEC_BASE_ROUTE"
    |     
    |     override val deepLinks: List<NavDeepLink> = $PLACEHOLDER_NAV_SPEC_DEEPLINK_VALUE
    |     
    | }
    """.trimMargin("| ")



    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Template for DestinationArgSpec


    const val PLACEHOLDER_NAV_ARG_SPEC_ROUTE_ARGS = "ROUTE_PLACEHOLDER"
    const val PLACEHOLDER_NAV_ARG_SPEC_NAV_ARG_TYPE = "NAV_ARG_TYPE_PLACEHOLDER"
    const val PLACEHOLDER_NAV_ARG_SPEC_INVOKE_FUNCTION_PARAMETER = "INVOKE_PARAMETER_PLACEHOLDER"
    const val PLACEHOLDER_NAV_ARG_SPEC_INVOKE_FUNCTION_BODY = "INVOKE_BODY_PLACEHOLDER"
    const val PLACEHOLDER_NAV_ARG_SPEC_NAMED_ARGUMENTS = "NAMED_ARGUMENTS_PLACEHOLDER"
    const val PLACEHOLDER_NAV_ARG_SPEC_GET_ARGS_BACKSTACK = "NAV_ARGS_BACK_STACK_PLACEHOLDER"
    const val PLACEHOLDER_NAV_ARG_SPEC_GET_ARGS_SAVED_STATE = "NAV_ARGS_SAVED_STATE_PLACEHOLDER"

    val NAV_DESTINATION_ARG_SPEC_TEMPLATE =
    """
    | object $PLACEHOLDER_NAV_SPEC_DESTINATION_NAME: ${PackageUtils.NAV_DESTINATION_ARG_SPEC_IMPORT.simpleName}<$PLACEHOLDER_NAV_ARG_SPEC_NAV_ARG_TYPE> {
    |     
    |     override val baseRoute: String = "$PLACEHOLDER_NAV_SPEC_BASE_ROUTE"
    |     
    |     override val route: String = baseRoute + 
    |         $PLACEHOLDER_NAV_ARG_SPEC_ROUTE_ARGS
    |         
    |     operator fun invoke(
    |         $PLACEHOLDER_NAV_ARG_SPEC_INVOKE_FUNCTION_PARAMETER
    |     ) = object: Routable {
    |         override val parameterizedRoute: String = $PLACEHOLDER_NAV_ARG_SPEC_INVOKE_FUNCTION_BODY
    |     }
    |         
    |     override fun argsFrom(navBackStackEntry: NavBackStackEntry) = $PLACEHOLDER_NAV_ARG_SPEC_NAV_ARG_TYPE(
    |         $PLACEHOLDER_NAV_ARG_SPEC_GET_ARGS_BACKSTACK
    |     )
    | 
    |     override fun argsFrom(savedStateHandle: SavedStateHandle) = $PLACEHOLDER_NAV_ARG_SPEC_NAV_ARG_TYPE(
    |         $PLACEHOLDER_NAV_ARG_SPEC_GET_ARGS_SAVED_STATE
    |     )
    | 
    |     override val arguments: List<NamedNavArgument> = listOf(
    |         $PLACEHOLDER_NAV_ARG_SPEC_NAMED_ARGUMENTS
    |     )
    | 
    |     override val deepLinks: List<NavDeepLink> = emptyList()
    |           
    | }
    """.trimMargin("| ")



    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Template for NavDestinationUtils File


    const val PLACEHOLDER_NAV_UTILS_DESTINATION_SPEC_MAP = "NAV_UTILS_DESTINATION_SPEC_MAP_PLACEHOLDER"

    val NAV_DESTINATION_UTILS_TEMPLATE =
    """
    | object ${NAV_DESTINATION_UTILS_FILE_IMPORT.simpleName} {
    |     
    |     val allSpecs: Map<Destination<out DestinationScope>, DestinationSpec> = mapOf(
    |         $PLACEHOLDER_NAV_UTILS_DESTINATION_SPEC_MAP
    |     )
    |    
    | }
    """.trimMargin("| ")





    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Template for Destination Extension File


    private val DESTINATION_FIND_SPEC_EXTENSION_FUNCTIONS =
    """
    | fun Destination<out DestinationScope>.findSpec(): DestinationSpec = ${NAV_DESTINATION_UTILS_FILE_IMPORT.simpleName}.allSpecs[this] 
    |     ?: throw IllegalArgumentException("Destination is not annotated with NavDestinationDefinition")
    | 
    | @Suppress("UNCHECKED_CAST")
    | fun <Arg : Any> ArgDestination<Arg>.findArgSpec(): ArgDestinationSpec<Arg> = findSpec() as ArgDestinationSpec<Arg>
    | 
    | fun PlainDestination.findPlainSpec(): PlainDestinationSpec = findSpec() as PlainDestinationSpec
    | 
    | val Destination<out DestinationScope>.route get() = findSpec().route
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
    // Template for Result Extensions File


    private val DESTINATION_SCOPE_SEND_RESULT_TO_DESTINATION_EXTENSION_FUNCTION =
    """
    | inline fun <reified T> DestinationScope.sendDestinationResultTo(
    |     destination: Destination<*>,
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
    |     destination: Destination<*>,
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
    |     forDestination: Destination<*>,
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
    |     forDestination: Destination<*>,
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



    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Template for NavGraphCreation -> NavGraph sollte dann auch als Variable in den Destinations drin sein



    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Template for NavGraphBuilder composable and navigation


}