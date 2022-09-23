package com.welu.composenavdestinations.generation.templates

import com.welu.composenavdestinations.utils.PackageUtils.NAV_COMPONENT_UTILS_FILE_IMPORT

internal object GeneralCodeTemplates {


    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Template for NavComponentUtils File

    const val PLACEHOLDER_NAV_UTILS_DESTINATION_SPEC_MAP = "NAV_UTILS_DESTINATION_SPEC_MAP_PLACEHOLDER"

    val NAV_COMPONENT_UTILS_TEMPLATE =
    """
    | object ${NAV_COMPONENT_UTILS_FILE_IMPORT.simpleName} {
    |     
    |     val allSpecs: Map<Destination<out DestinationScope>, DestinationSpec<*>> = mapOf(
    |         $PLACEHOLDER_NAV_UTILS_DESTINATION_SPEC_MAP
    |     )
    |    
    | }
    """.trimMargin("| ")


    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Template for Destination Extension File

    private val DESTINATION_FIND_SPEC_EXTENSION_FUNCTIONS =
    """
    | fun Destination<out DestinationScope>.findSpec(): DestinationSpec<*> = ${NAV_COMPONENT_UTILS_FILE_IMPORT.simpleName}.allSpecs[this] 
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

}