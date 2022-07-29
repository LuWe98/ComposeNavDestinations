package com.welu.composenavdestinations.annotaioninfo

import com.welu.composenavdestinations.utils.Constants

object NavDestinationAnnotation: AnnotationDeclaration {
    override val name = "NavDestination"
    override val identifier = "${Constants.PACKAGE_NAME}.annotations.$name"
    const val routeArg = "route"
}