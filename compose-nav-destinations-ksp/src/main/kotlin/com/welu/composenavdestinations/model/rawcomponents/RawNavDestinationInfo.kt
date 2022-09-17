package com.welu.composenavdestinations.model.rawcomponents

import com.google.devtools.ksp.symbol.KSClassDeclaration

data class RawNavDestinationInfo(
    override val isStart: Boolean = false,
    override val baseRoute: String,
    override val classDeclaration: KSClassDeclaration,
    val parentNavGraph: KSClassDeclaration
): RawNavComponentInfo {
    val simpleName get() = classDeclaration.simpleName.asString()
}
