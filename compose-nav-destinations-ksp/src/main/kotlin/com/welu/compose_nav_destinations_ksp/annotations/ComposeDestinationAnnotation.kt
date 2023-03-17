package com.welu.compose_nav_destinations_ksp.annotations

object ComposeDestinationAnnotation: AnnotationDeclaration("ComposeDestination") {
    const val ROUTE_ARG = "route"
    const val DEEP_LINK_ARG = "deepLinks"
}