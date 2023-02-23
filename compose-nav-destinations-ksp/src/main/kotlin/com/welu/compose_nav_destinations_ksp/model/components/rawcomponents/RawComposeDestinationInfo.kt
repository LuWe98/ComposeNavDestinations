package com.welu.compose_nav_destinations_ksp.model.components.rawcomponents

import com.google.devtools.ksp.symbol.KSClassDeclaration

data class RawComposeDestinationInfo(
    override val isStart: Boolean = false,
    override val baseRoute: String,
    override val classDeclaration: KSClassDeclaration,
    val parentNavGraph: KSClassDeclaration
): RawNavComponentInfo {
    val simpleName get() = classDeclaration.simpleName.asString()
}
