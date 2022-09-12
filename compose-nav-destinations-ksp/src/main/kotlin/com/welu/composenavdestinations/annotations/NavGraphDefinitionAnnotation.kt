package com.welu.composenavdestinations.annotations

object NavGraphDefinitionAnnotation: AnnotationDeclaration("NavGraphDefinition") {
    const val ROUTE_ARG = "isStart"
    const val IS_DEFAULT_NAV_GRAPH = "isDefaultNavGraph"
    const val ARGS_CLASS_ARG = "argsClass"
    const val DEEP_LINKS_ARG = "deepLinks"
}