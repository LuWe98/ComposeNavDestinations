package com.welu.composenavdestinations.model

/**
 * @property route The route this destination is uniquely identified with
 * @property destinationImport The import for the annotated Destination
 * @property destinationSpecImport The Import for the generated DestinationSpec for this Destination
 * @property navArgsInfo The NavArgs generic type info. Only present when the Destination is an ArgDestination
 */
data class NavDestinationInfo(
    val route: String,
    val destinationImport: ImportInfo,
    val destinationSpecImport: ImportInfo,
    val navArgsInfo: NavArgsInfo? = null
) {

    val packageName get() = destinationSpecImport.packageDir
    val simpleName get() = destinationSpecImport.simpleName
    val isArgDestination get() = navArgsInfo != null

    val allImports: Set<ImportInfo> get() = mutableSetOf<ImportInfo>().apply {
        add(destinationImport)
        navArgsInfo?.let {
            addAll(it.parameters.flatMap(Parameter::imports).filter(ImportInfo::isNonDefaultPackage))
            addAll(it.typeInfo.allChildImports)

        }
    }

}