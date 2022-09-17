package com.welu.composenavdestinations.generation.graphs

object NavGraphCodeTemplates {

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Template for NavGraphCreation -> NavGraph sollte dann auch als Variable in den Destinations drin sein

    const val PLACEHOLDER_NAV_GRAPH_SPEC_NAME = "{PLACEHOLDER-NAV-GRAPH-SPEC-NAME}"
    const val PLACEHOLDER_NAV_GRAPH_SPEC_BASE_ROUTE = "{PLACEHOLDER-BASE-ROUTE}"
    const val PLACEHOLDER_NAV_GRAPH_SPEC_START_COMPONENT = "{PLACEHOLDER-START-COMPONENT}"
    const val PLACEHOLDER_NAV_GRAPH_SPEC_PARENT_NAV_GRAPH_SPEC = "{Placeholder-Parent-NavGraphSpec}"
    const val PLACEHOLDER_NAV_GRAPH_SPEC_CHILD_NAV_COMPONENT_SPECS = "{Placeholder-Child-NavComponentSpecs}"

    val NAV_GRAPH_SPEC_TEMPLATE =
    """
    |object $PLACEHOLDER_NAV_GRAPH_SPEC_NAME: NavGraphSpec {
    |  
    |    override val baseRoute: String = "$PLACEHOLDER_NAV_GRAPH_SPEC_BASE_ROUTE"
    |  
    |    override val route get(): String = baseRoute
    |    
    |    override val startComponentSpec get(): NavComponentSpec = $PLACEHOLDER_NAV_GRAPH_SPEC_START_COMPONENT
    |    
    |    override val parentNavGraphSpec get(): NavGraphSpec? = $PLACEHOLDER_NAV_GRAPH_SPEC_PARENT_NAV_GRAPH_SPEC
    |    
    |    override val childNavComponentSpecs: List<NavComponentSpec> = listOf(
    |        $PLACEHOLDER_NAV_GRAPH_SPEC_CHILD_NAV_COMPONENT_SPECS
    |    )
    |     
    |}
    """.trimMargin("|")

}