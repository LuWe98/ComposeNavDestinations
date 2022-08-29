package com.welu.composenavdestinations.annotationinfo

import com.welu.composenavdestinations.model.ImportInfo
import com.welu.composenavdestinations.utils.PackageUtils

sealed class AnnotationDeclaration(
    val name: String,
    val import: ImportInfo =  ImportInfo(name,"${PackageUtils.PACKAGE_NAME}.annotations")
)

object NavArgumentAnnotation: AnnotationDeclaration("NavArgument") {
    const val NAME_ARG = "name"
}

object NavDestinationAnnotation: AnnotationDeclaration("NavDestination") {
    const val ROUTE_ARG = "route"
    const val NAV_ARGS_ARG = "navArgs"
}