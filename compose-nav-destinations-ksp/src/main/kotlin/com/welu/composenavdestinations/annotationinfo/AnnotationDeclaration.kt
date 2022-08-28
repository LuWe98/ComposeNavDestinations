package com.welu.composenavdestinations.annotationinfo

import com.welu.composenavdestinations.utils.PackageUtils

sealed class AnnotationDeclaration(
    val name: String,
    val identifier: String = "${PackageUtils.PACKAGE_NAME}.annotations.$name"
)

object NavArgumentAnnotation: AnnotationDeclaration("NavArgument") {
    const val NAME_ARG = "name"
}

object NavDestinationAnnotation: AnnotationDeclaration("NavDestination") {
    const val ROUTE_ARG = "route"
    const val NAV_ARGS_ARG = "navArgs"
}