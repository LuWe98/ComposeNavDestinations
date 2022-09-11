package com.welu.composenavdestinations.model

data class NavDestinationInfo(
    val destinationImport: ImportInfo,
    val destinationSpecImport: ImportInfo,
    val route: String,
    // Wenn das null ist, muss mit den Parametern eine neue klasse generiert werden, sonst einfach diese Klasse importieren
    val navArgsTypeInfo: ParameterTypeInfo? = null,
    val parameters: List<Parameter> = emptyList()
) {

    val packageName get() = destinationSpecImport.packageDir
    val simpleName get() = destinationSpecImport.simpleName
    val isArgDestination get() = navArgsTypeInfo != null

    val allImports: Set<ImportInfo> get() = mutableSetOf<ImportInfo>().apply {
        addAll(parameters.flatMap(Parameter::imports).filter(ImportInfo::isNonDefaultPackage))
        add(destinationImport)
        navArgsTypeInfo?.allChildImports?.let(::addAll)
    }

}