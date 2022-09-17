package com.welu.composenavdestinations.generation.graphs

import com.welu.composenavdestinations.generation.FileContentInfoGenerator
import com.welu.composenavdestinations.model.FileContentInfo
import com.welu.composenavdestinations.model.components.NavGraphInfo
import com.welu.composenavdestinations.utils.PackageUtils

//TODO -> noch einbauen
object FileGeneratorNavGraphSpec : FileContentInfoGenerator<NavGraphInfo> {

    override fun generate(instance: NavGraphInfo): FileContentInfo? = if (instance.isArgNavGraph) {
        null
    } else {
        generatePlainNavGraphSpec(instance)
    }

    private fun generatePlainNavGraphSpec(plainNavGraphInfo: NavGraphInfo) = FileContentInfo(
        fileName = plainNavGraphInfo.simpleName,
        packageDir = PackageUtils.NAV_GRAPH_SPEC_PACKAGE,
        imports = mutableSetOf(
            PackageUtils.ANDROID_NAVIGATION_DEEP_LINK_IMPORT,
            PackageUtils.NAV_COMPONENT_SPEC_IMPORT,
            PackageUtils.NAV_GRAPH_SPEC_IMPORT
        ).apply {
            addAll(plainNavGraphInfo.allImports)
        },
        code = NavGraphCodeTemplates.NAV_GRAPH_SPEC_TEMPLATE
            .replace(NavGraphCodeTemplates.PLACEHOLDER_NAV_GRAPH_SPEC_NAME, plainNavGraphInfo.specImport.simpleName)
            .replace(NavGraphCodeTemplates.PLACEHOLDER_NAV_GRAPH_SPEC_BASE_ROUTE, plainNavGraphInfo.baseRoute)
            .replace(NavGraphCodeTemplates.PLACEHOLDER_NAV_GRAPH_SPEC_PARENT_NAV_GRAPH_SPEC, plainNavGraphInfo.parentNavGraphSpecImport?.simpleName ?: "null")
            .replace(NavGraphCodeTemplates.PLACEHOLDER_NAV_GRAPH_SPEC_START_COMPONENT, plainNavGraphInfo.startComponentDeclaration.simpleName)
            .replace(NavGraphCodeTemplates.PLACEHOLDER_NAV_GRAPH_SPEC_CHILD_NAV_COMPONENT_SPECS, generateChildNavComponentSpecParams(plainNavGraphInfo))
    )

    private fun generateChildNavComponentSpecParams(navGraphInfo: NavGraphInfo): String = navGraphInfo.allChildNavComponentSpecImports.joinToString(",\n\t\t") {
        it.simpleName
    }

}