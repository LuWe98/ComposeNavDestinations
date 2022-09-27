package com.welu.composenavdestinations.annotations

object ComposeNavGraphAnnotation : AnnotationDeclaration("ComposeNavGraph") {
    const val ROUTE_ARG = "route"
    const val IS_DEFAULT_NAV_GRAPH = "isDefaultNavGraph"
    const val ARGS_CLASS_ARG = "argsClass"
    const val DEEP_LINKS_ARG = "deepLinks"
}