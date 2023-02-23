package com.welu.compose_nav_destinations_ksp.model.components

import com.welu.compose_nav_destinations_ksp.model.ImportInfo
import com.welu.compose_nav_destinations_ksp.model.navargs.NavArgsInfo

/**
 * @property baseRoute The not parameterized route of this NavComponent
 * @property specImport The import of this NavComponentSpec - Contains the Name and PackageDirectory
 * @property deepLinks The DeepLinks to this [NavComponentInfo]
 * @property navArgsInfo Contains information about the NavArgs to this [NavComponentInfo]
 * @property parentNavGraphSpecImport The import for the parent [ComposeNavGraphInfo] this [NavComponentInfo] is contained in
 */
sealed interface NavComponentInfo {
    val specImport: ImportInfo
    val baseRoute: String
    val deepLinks: List<String>
    val navArgsInfo: NavArgsInfo?
    val parentNavGraphSpecImport: ImportInfo?
}