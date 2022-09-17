package com.welu.composenavdestinations.model.components

import com.welu.composenavdestinations.model.ImportInfo
import com.welu.composenavdestinations.model.NavArgsInfo
import com.welu.composenavdestinations.model.Parameter

/**
 * @property startComponentDeclaration The [ImportInfo] of this [NavComponentInfo] - Is the startDestination of this NavGraph
 * @property childNavGraphSpecImports The [ImportInfo]s of the child [NavGraphInfo]s
 * @property childNavDestinationSpecImports The [ImportInfo]s of the child [NavDestinationInfo]s
 */
data class NavGraphInfo(
    override val baseRoute: String,
    override val specImport: ImportInfo,
    override val deepLinks: List<String> = emptyList(),
    override val navArgsInfo: NavArgsInfo? = null,
    override val parentNavGraphSpecImport: ImportInfo? = null,
    val startComponentDeclaration: ImportInfo,
    val childNavGraphSpecImports: List<ImportInfo> = emptyList(),
    val childNavDestinationSpecImports: List<ImportInfo> = emptyList()
) : NavComponentInfo {

    val simpleName get() = specImport.simpleName
    val packageDir get() = specImport.packageDir
    val isArgNavGraph get() = navArgsInfo != null

    val allChildNavComponentSpecImports get() = childNavDestinationSpecImports + childNavGraphSpecImports

    val allImports get() = mutableSetOf(startComponentDeclaration).apply {
        parentNavGraphSpecImport?.let(::add)
        addAll(childNavGraphSpecImports)
        addAll(childNavDestinationSpecImports)
        navArgsInfo?.let {
            addAll(it.parameters.flatMap(Parameter::imports).filter(ImportInfo::isNonDefaultPackage))
            addAll(it.typeInfo.allImports)
        }
    }
}