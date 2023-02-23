package com.welu.compose_nav_destinations_ksp.annotations

object ComposeNavGraphAnnotation : com.welu.compose_nav_destinations_ksp.annotations.AnnotationDeclaration("ComposeNavGraph") {
    const val ROUTE_ARG = "route"
    const val IS_DEFAULT_NAV_GRAPH = "isDefaultNavGraph"
    const val ARGS_CLASS_ARG = "argsClass"
    const val DEEP_LINKS_ARG = "deepLinks"
}