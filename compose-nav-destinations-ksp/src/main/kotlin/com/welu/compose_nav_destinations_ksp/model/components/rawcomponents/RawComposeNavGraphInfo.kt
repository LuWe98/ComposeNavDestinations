package com.welu.compose_nav_destinations_ksp.model.components.rawcomponents

import com.google.devtools.ksp.symbol.KSClassDeclaration

data class RawComposeNavGraphInfo(
    override val classDeclaration: KSClassDeclaration,
    override val baseRoute: String,
    override val isStart: Boolean,
    val isDefaultNavGraph: Boolean,
    val startComponentDeclaration: RawNavComponentInfo? = null,
    val parentNavGraphSpecDeclaration: KSClassDeclaration? = null,
    val childNavGraphSpecDeclarations: List<KSClassDeclaration> = emptyList(),
    val childNavDestinationSpecDeclarations: List<KSClassDeclaration> = emptyList()
): RawNavComponentInfo {
    val simpleName get() = classDeclaration.simpleName.asString()
}