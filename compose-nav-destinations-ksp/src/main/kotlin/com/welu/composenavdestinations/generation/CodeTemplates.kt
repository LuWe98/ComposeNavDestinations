package com.welu.composenavdestinations.generation

import com.welu.composenavdestinations.utils.PackageUtils.NAV_DESTINATION_UTILS_FILE_NAME

internal object CodeTemplates {

    //GENERAL
    const val PLACEHOLDER_NAV_SPEC_BASE_ROUTE = "BASE_ROUTE_PLACEHOLDER"
    const val PLACEHOLDER_NAV_SPEC_DESTINATION_NAME = "DESTINATION_NAME_PLACEHOLDER"
    const val PLACEHOLDER_NAV_SPEC_DEEPLINK_VALUE = "DEEPLINK_VALUE_PLACEHOLDER"


    val NAV_DESTINATION_PLAIN_SPEC_TEMPLATE = """
    object $PLACEHOLDER_NAV_SPEC_DESTINATION_NAME: NavDestinationPlainSpec {
        override val baseRoute = "$PLACEHOLDER_NAV_SPEC_BASE_ROUTE"
        override val deepLinks: List<NavDeepLink> = $PLACEHOLDER_NAV_SPEC_DEEPLINK_VALUE
    }
""".trimIndent()



    //With NAV ARGS
    const val PLACEHOLDER_NAV_ARG_SPEC_ROUTE_ARGS = "ROUTE_PLACEHOLDER"
    const val PLACEHOLDER_NAV_ARG_SPEC_NAV_ARG_TYPE = "NAV_ARG_TYPE_PLACEHOLDER"
    const val PLACEHOLDER_NAV_ARG_SPEC_GENERATED_NAV_ARG = "GENERATED_NAV_ARG_PLACEHOLDER"
    const val PLACEHOLDER_NAV_ARG_SPEC_INVOKE_FUNCTION_PARAMETER = "INVOKE_PARAMETER_PLACEHOLDER"
    const val PLACEHOLDER_NAV_ARG_SPEC_INVOKE_FUNCTION_BODY = "INVOKE_BODY_PLACEHOLDER"
    const val PLACEHOLDER_NAV_ARG_SPEC_NAMED_ARGUMENTS = "NAMED_ARGUMENTS_PLACEHOLDER"
    const val PLACEHOLDER_NAV_ARG_SPEC_GET_ARGS_BACKSTACK = "NAV_ARGS_BACK_STACK_PLACEHOLDER"
    const val PLACEHOLDER_NAV_ARG_SPEC_GET_ARGS_SAVED_STATE = "NAV_ARGS_SAVED_STATE_PLACEHOLDER"

    val NAV_DESTINATION_ARG_SPEC_TEMPLATE = """
    object $PLACEHOLDER_NAV_SPEC_DESTINATION_NAME: NavDestinationArgSpec<$PLACEHOLDER_NAV_ARG_SPEC_NAV_ARG_TYPE> {
        
        override val baseRoute: String = "$PLACEHOLDER_NAV_SPEC_BASE_ROUTE"
        
        override val route: String = baseRoute + 
            $PLACEHOLDER_NAV_ARG_SPEC_ROUTE_ARGS
            
        operator fun invoke(
            $PLACEHOLDER_NAV_ARG_SPEC_INVOKE_FUNCTION_PARAMETER
        ) = object: NavDestinationRoute {
            override val parameterizedRoute: String = $PLACEHOLDER_NAV_ARG_SPEC_INVOKE_FUNCTION_BODY
        }
            
        override fun getArgs(navBackStackEntry: NavBackStackEntry) = $PLACEHOLDER_NAV_ARG_SPEC_NAV_ARG_TYPE(
            $PLACEHOLDER_NAV_ARG_SPEC_GET_ARGS_BACKSTACK
        )

        override fun getArgs(savedStateHandle: SavedStateHandle) = $PLACEHOLDER_NAV_ARG_SPEC_NAV_ARG_TYPE(
            $PLACEHOLDER_NAV_ARG_SPEC_GET_ARGS_SAVED_STATE
        )
    
        override val arguments: List<NamedNavArgument> = listOf(
            $PLACEHOLDER_NAV_ARG_SPEC_NAMED_ARGUMENTS
        )

        override val deepLinks: List<NavDeepLink> = emptyList()
       
        $PLACEHOLDER_NAV_ARG_SPEC_GENERATED_NAV_ARG
        
    }
""".trimIndent()



    //Nav Utils
    const val PLACEHOLDER_NAV_UTILS_ALL_DESTINATIONS = "NAV_UTILS_ALL_DESTINATIONS_PLACEHOLDER"

    val NAV_DESTINATION_UTILS_TEMPLATE = """
    object $NAV_DESTINATION_UTILS_FILE_NAME {
        
        val allDestinations: List<NavDestinationSpec> = listOf(
            $PLACEHOLDER_NAV_UTILS_ALL_DESTINATIONS
        )
       
    }
""".trimIndent()
}