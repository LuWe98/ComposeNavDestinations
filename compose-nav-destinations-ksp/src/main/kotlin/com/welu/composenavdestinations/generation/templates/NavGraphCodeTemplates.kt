package com.welu.composenavdestinations.generation.templates

object NavGraphCodeTemplates {

    const val PLACEHOLDER_NAV_GRAPH_SPEC_NAME = "{PLACEHOLDER-NAV-GRAPH-SPEC-NAME}"
    const val PLACEHOLDER_NAV_GRAPH_SPEC_BASE_ROUTE = "{PLACEHOLDER-BASE-ROUTE}"
    const val PLACEHOLDER_NAV_GRAPH_SPEC_START_COMPONENT = "{PLACEHOLDER-START-COMPONENT}"
    const val PLACEHOLDER_NAV_GRAPH_SPEC_PARENT_NAV_GRAPH_SPEC = "{Placeholder-Parent-NavGraphSpec}"
    const val PLACEHOLDER_NAV_GRAPH_SPEC_CHILD_NAV_COMPONENT_SPECS = "{Placeholder-Child-NavComponentSpecs}"

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Template for PlainNavGraphs

    val NAV_GRAPH_PLAIN_SPEC_TEMPLATE =
    """
    |object $PLACEHOLDER_NAV_GRAPH_SPEC_NAME: PlainNavGraphSpec {
    |  
    |    override val baseRoute: String = "$PLACEHOLDER_NAV_GRAPH_SPEC_BASE_ROUTE"
    |  
    |    override val startComponentSpec get(): NavComponentSpec = $PLACEHOLDER_NAV_GRAPH_SPEC_START_COMPONENT
    |    
    |    override val parentNavGraphSpec get(): NavGraphSpec? = $PLACEHOLDER_NAV_GRAPH_SPEC_PARENT_NAV_GRAPH_SPEC
    |    
    |    override val childNavComponentSpecs: List<NavComponentSpec> = listOf(
    |        $PLACEHOLDER_NAV_GRAPH_SPEC_CHILD_NAV_COMPONENT_SPECS
    |    )
    |     
    |    override val deepLinks: List<NavDeepLink> = emptyList()
    |     
    |}
    """.trimMargin("|")



    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Template for ArgNavGraphs

    const val PLACEHOLDER_NAV_ARG_SPEC_ROUTE_ARGS = "ROUTE_PLACEHOLDER"
    const val PLACEHOLDER_NAV_ARG_SPEC_NAV_ARG_TYPE = "NAV_ARG_TYPE_PLACEHOLDER"
    const val PLACEHOLDER_NAV_ARG_SPEC_INVOKE_FUNCTION_PARAMETER = "INVOKE_PARAMETER_PLACEHOLDER"
    const val PLACEHOLDER_NAV_ARG_SPEC_INVOKE_FUNCTION_BODY = "INVOKE_BODY_PLACEHOLDER"
    const val PLACEHOLDER_NAV_ARG_SPEC_NAMED_ARGUMENTS = "NAMED_ARGUMENTS_PLACEHOLDER"
    const val PLACEHOLDER_NAV_ARG_SPEC_GET_ARGS_BACKSTACK = "NAV_ARGS_BACK_STACK_PLACEHOLDER"


    val NAV_GRAPH_ARG_SPEC_TEMPLATE =
    """
    |object $PLACEHOLDER_NAV_GRAPH_SPEC_NAME: ArgNavGraphSpec<$PLACEHOLDER_NAV_ARG_SPEC_NAV_ARG_TYPE> {
    |  
    |    override val baseRoute: String = "$PLACEHOLDER_NAV_GRAPH_SPEC_BASE_ROUTE"
    |  
    |    override val route: String = baseRoute + 
    |         $PLACEHOLDER_NAV_ARG_SPEC_ROUTE_ARGS 
    |     
    |    override val startComponentSpec get(): NavComponentSpec = $PLACEHOLDER_NAV_GRAPH_SPEC_START_COMPONENT
    |    
    |    override val parentNavGraphSpec get(): NavGraphSpec? = $PLACEHOLDER_NAV_GRAPH_SPEC_PARENT_NAV_GRAPH_SPEC
    |    
    |    override val childNavComponentSpecs: List<NavComponentSpec> = listOf(
    |        $PLACEHOLDER_NAV_GRAPH_SPEC_CHILD_NAV_COMPONENT_SPECS
    |    )
    |     
    |     override val deepLinks: List<NavDeepLink> = emptyList()
    |      
    |     override val arguments: List<NamedNavArgument> = listOf(
    |         $PLACEHOLDER_NAV_ARG_SPEC_NAMED_ARGUMENTS
    |     )
    |     
    |     override fun argsFrom(navBackStackEntry: NavBackStackEntry) = $PLACEHOLDER_NAV_ARG_SPEC_NAV_ARG_TYPE(
    |         $PLACEHOLDER_NAV_ARG_SPEC_GET_ARGS_BACKSTACK
    |     )
    |     
    |     operator fun invoke(
    |         $PLACEHOLDER_NAV_ARG_SPEC_INVOKE_FUNCTION_PARAMETER
    |     ) = object: Routable {
    |         override val parameterizedRoute: String = $PLACEHOLDER_NAV_ARG_SPEC_INVOKE_FUNCTION_BODY
    |     }
    |     
    |}
    """.trimMargin("|")

}