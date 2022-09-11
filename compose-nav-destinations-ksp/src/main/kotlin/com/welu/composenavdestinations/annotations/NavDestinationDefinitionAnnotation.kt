package com.welu.composenavdestinations.annotations

object NavDestinationDefinitionAnnotation: AnnotationDeclaration("NavDestinationDefinition") {
    const val ROUTE_ARG = "route"
    const val DESTINATION_TYPE_ARG = "destinationType"
    const val DEEP_LINK_ARG = "deepLinks"
}