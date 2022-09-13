package com.welu.composenavdestinations.model.structure

import com.google.devtools.ksp.symbol.KSClassDeclaration

data class NavGraphStructure(
    val route: String = "",
    val isStart: Boolean = false,
    val isDefaultNavGraph: Boolean = false,
    val classDeclaration: KSClassDeclaration,
    val nestedNavGraphs: List<NavGraphStructure> = emptyList(),
    val navDestinations: List<NavDestinationStructure> = emptyList()
) {
    val simpleName get() = classDeclaration.simpleName.asString()
}
