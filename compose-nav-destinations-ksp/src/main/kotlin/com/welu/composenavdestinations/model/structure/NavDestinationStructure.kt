package com.welu.composenavdestinations.model.structure

import com.google.devtools.ksp.symbol.KSClassDeclaration

data class NavDestinationStructure(
    val isStart: Boolean = false,
    val classDeclaration: KSClassDeclaration
) {
    val simpleName get() = classDeclaration.simpleName.asString()
}
