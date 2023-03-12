package com.welu.compose_nav_destinations_ksp.generation.general

import com.welu.compose_nav_destinations_ksp.generation.FileContentInfoTypedGenerator
import com.welu.compose_nav_destinations_ksp.generation.templates.CodeTemplates
import com.welu.compose_nav_destinations_ksp.generation.templates.CodeTemplates.PLACEHOLDER_ROOT_NAV_GRAPH_SPECS_ARGUMENT
import com.welu.compose_nav_destinations_ksp.model.FileContentInfo
import com.welu.compose_nav_destinations_ksp.model.ImportInfo
import com.welu.compose_nav_destinations_ksp.model.components.ComposeDestinationInfo
import com.welu.compose_nav_destinations_ksp.model.components.ComposeNavGraphInfo
import com.welu.compose_nav_destinations_ksp.utils.PackageUtils


object FileComposeNavDestinationsExtensions : FileContentInfoTypedGenerator<Sequence<ComposeNavGraphInfo>> {

    override fun generate(instance: Sequence<ComposeNavGraphInfo>): FileContentInfo {
        val rootNavGraphs = instance.filter(ComposeNavGraphInfo::isRoot)

        return FileContentInfo(
            fileImportInfo = PackageUtils.COMPOSE_NAV_DESTINATIONS_EXTENSION_FILE_IMPORT,
            imports = mutableSetOf<ImportInfo>().apply {
                add(PackageUtils.COMPOSE_NAV_DESTINATIONS_IMPORT)
                rootNavGraphs.forEach {
                    add(it.specImport)
                }
            },
            code = CodeTemplates.NAV_COMPOSE_NAV_DESTINATIONS_EXTENSIONS_TEMPLATE.replace(
                PLACEHOLDER_ROOT_NAV_GRAPH_SPECS_ARGUMENT, rootNavGraphs.joinToString(",") {
                    it.simpleName
                }
            )
        )
    }
}