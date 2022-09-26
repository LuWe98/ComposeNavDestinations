package com.welu.composenavdestinations.model.components

import com.welu.composenavdestinations.model.ImportInfo
import com.welu.composenavdestinations.model.NavArgsInfo
import com.welu.composenavdestinations.model.Parameter

/**
 * @property destinationImport The import for the annotated Destination
 * @property parentNavGraphSpecImport The import for the parent [NavGraphInfo] this [NavDestinationInfo] is contained in
 */
data class NavDestinationInfo(
    override val baseRoute: String,
    override val specImport: ImportInfo,
    override val deepLinks: List<String> = emptyList(),
    override val navArgsInfo: NavArgsInfo? = null,
    override val parentNavGraphSpecImport: ImportInfo,
    val destinationType: NavDestinationType,
    val destinationImport: ImportInfo
) : NavComponentInfo {

    val packageDir get() = specImport.packageDir
    val simpleName get() = specImport.simpleName
    val isArgDestination get() = navArgsInfo != null

    val allImports: Set<ImportInfo>
        get() = mutableSetOf<ImportInfo>().apply {
            add(destinationImport)
            navArgsInfo?.let {
                addAll(it.parameters.flatMap(Parameter::imports).filter(ImportInfo::isNonDefaultPackage))
                addAll(it.typeInfo.allImports)
            }
        }
}