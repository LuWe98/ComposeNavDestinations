package com.welu.composenavdestinations.generation.destinations

import com.welu.composenavdestinations.utils.PackageUtils

object NavDestinationCodeTemplates {

    const val PLACEHOLDER_NAV_DESTINATION_NAME = "PLACEHOLDER_NAV_DESTINATION_NAME"
    const val PLACEHOLDER_NAV_DESTINATION_NAV_GRAPH = "PLACEHOLDER_NAV_DESTINATION_NAV_GRAPH"

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Template for DestinationPlainSpec

    const val PLACEHOLDER_NAV_SPEC_BASE_ROUTE = "BASE_ROUTE_PLACEHOLDER"
    const val PLACEHOLDER_NAV_SPEC_DESTINATION_NAME = "DESTINATION_NAME_PLACEHOLDER"
    const val PLACEHOLDER_NAV_SPEC_DEEPLINK_VALUE = "DEEPLINK_VALUE_PLACEHOLDER"

    val NAV_DESTINATION_PLAIN_SPEC_TEMPLATE =
    """
    | object $PLACEHOLDER_NAV_SPEC_DESTINATION_NAME: ${PackageUtils.NAV_DESTINATION_PLAIN_SPEC_IMPORT.simpleName} {
    | 
    |     override val destination get() = $PLACEHOLDER_NAV_DESTINATION_NAME
    |     
    |     override val parentNavGraphSpec get() = $PLACEHOLDER_NAV_DESTINATION_NAV_GRAPH
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
    |     override val destination get() = $PLACEHOLDER_NAV_DESTINATION_NAME
    |     
    |     override val parentNavGraphSpec get() = $PLACEHOLDER_NAV_DESTINATION_NAV_GRAPH
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

}