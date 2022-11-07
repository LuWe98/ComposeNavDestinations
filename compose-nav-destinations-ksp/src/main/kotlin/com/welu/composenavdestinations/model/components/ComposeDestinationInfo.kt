package com.welu.composenavdestinations.model.components

import com.welu.composenavdestinations.model.ImportInfo
import com.welu.composenavdestinations.model.navargs.NavArgsInfo
import com.welu.composenavdestinations.model.Parameter

/**
 * @property destinationImport The import for the annotated Destination
 * @property parentNavGraphSpecImport The import for the parent [ComposeNavGraphInfo] this [ComposeDestinationInfo] is contained in
 */
data class ComposeDestinationInfo(
    override val baseRoute: String,
    override val specImport: ImportInfo,
    override val deepLinks: List<String> = emptyList(),
    override val navArgsInfo: NavArgsInfo? = null,
    override val parentNavGraphSpecImport: ImportInfo,
    val destinationType: ComposeDestinationType,
    val destinationImport: ImportInfo
) : NavComponentInfo {

    val packageDir get() = specImport.packageDir
    val simpleName get() = specImport.simpleName
    val isArgDestination get() = navArgsInfo != null

    val imports: Set<ImportInfo>
        get() = mutableSetOf(destinationImport).apply {
            navArgsInfo?.let {
                addAll(it.typeInfo.imports)
                it.parameters
                    .flatMap(Parameter::imports)
                    .filter(ImportInfo::isNonDefaultPackage)
                    .let(::addAll)
            }
        }

    val typeImports: Set<ImportInfo>
        get() = mutableSetOf(destinationImport).apply {
            navArgsInfo?.parameters
                ?.flatMap(Parameter::typeImports)
                ?.filter(ImportInfo::isNonDefaultPackage)
                ?.let(::addAll)
        }

}