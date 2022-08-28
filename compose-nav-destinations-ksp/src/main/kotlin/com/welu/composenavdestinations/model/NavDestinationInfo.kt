package com.welu.composenavdestinations.model

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration

data class NavDestinationInfo(
    val name: String,
    val route: String,
    val functionDeclaration: KSFunctionDeclaration,
    // Wenn das null ist, muss mit den Parametern eine neue klasse generiert werden, sonst einfach diese Klasse importieren
    val navArgsClass: KSClassDeclaration? = null,
    val parameters: List<Parameter> = emptyList()
) {
    val packageName get() = functionDeclaration.packageName.asString()
    val simpleName get() = functionDeclaration.simpleName.asString()

    val allImports get() = parameters
        .flatMap(Parameter::imports)
        .filter(ImportInfo::isNonDefaultPackage)
        .distinctBy(ImportInfo::qualifiedName)
}