package com.welu.composenavdestinations.model

import com.google.devtools.ksp.symbol.KSClassDeclaration

data class NavDestinationInfo(
    val import: ImportInfo,
    val route: String,
    // Wenn das null ist, muss mit den Parametern eine neue klasse generiert werden, sonst einfach diese Klasse importieren
    val navArgsClass: KSClassDeclaration? = null,
    val parameters: List<Parameter> = emptyList()
) {

    val packageName get() = import.packageDir
    val simpleName get() = import.simpleName

    val allImports get() = parameters
        .flatMap(Parameter::imports)
        .filter(ImportInfo::isNonDefaultPackage)
        .distinctBy(ImportInfo::qualifiedName)

}