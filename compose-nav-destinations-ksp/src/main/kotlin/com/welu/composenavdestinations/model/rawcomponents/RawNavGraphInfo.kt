package com.welu.composenavdestinations.model.rawcomponents

import com.google.devtools.ksp.symbol.KSClassDeclaration

data class RawNavGraphInfo(
    override val classDeclaration: KSClassDeclaration,
    override val baseRoute: String,
    override val isStart: Boolean,
    val isDefaultNavGraph: Boolean,
    val startComponentDeclaration: RawNavComponentInfo? = null,
    val parentNavGraphSpecDeclaration: KSClassDeclaration? = null,
    val childNavGraphSpecDeclarations: Sequence<KSClassDeclaration> = emptySequence(),
    val childNavDestinationSpecDeclarations: Sequence<KSClassDeclaration> = emptySequence()
): RawNavComponentInfo {
    val simpleName get() = classDeclaration.simpleName.asString()
}