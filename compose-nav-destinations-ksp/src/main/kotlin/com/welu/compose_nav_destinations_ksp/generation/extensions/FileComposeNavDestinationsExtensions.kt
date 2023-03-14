package com.welu.compose_nav_destinations_ksp.generation.extensions

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.welu.compose_nav_destinations_ksp.extensions.kotlinpoet.addImports
import com.welu.compose_nav_destinations_ksp.extensions.kotlinpoet.build
import com.welu.compose_nav_destinations_ksp.extensions.toClassName
import com.welu.compose_nav_destinations_ksp.generation.FileSpecGenerator
import com.welu.compose_nav_destinations_ksp.model.ImportInfo
import com.welu.compose_nav_destinations_ksp.model.components.ComposeNavGraphInfo
import com.welu.compose_nav_destinations_ksp.utils.PackageUtils
import com.welu.compose_nav_destinations_ksp.utils.PackageUtils.NAV_DESTINATIONS_EXTENSIONS_PACKAGE

object FileComposeNavDestinationsExtensions : FileSpecGenerator<Sequence<ComposeNavGraphInfo>> {

    private val FILE_IMPORT = ImportInfo("ComposeNavDestinationsExt", NAV_DESTINATIONS_EXTENSIONS_PACKAGE)

    override fun generate(input: Sequence<ComposeNavGraphInfo>): FileSpec? {
        val rootNavGraphs = input.filter(ComposeNavGraphInfo::isRoot)

        if (rootNavGraphs.none()) return null

        return FileSpec.build(FILE_IMPORT) {
            addFunction(generateInitExtension(rootNavGraphs))
            addImports(rootNavGraphs.map(ComposeNavGraphInfo::specImport))
        }
    }

    private fun generateInitExtension(
        rootNavGraphs: Sequence<ComposeNavGraphInfo>
    ): FunSpec {
        val specNames = rootNavGraphs.joinToString(",", transform = ComposeNavGraphInfo::simpleName)

        return FunSpec.build("init") {
            receiver(PackageUtils.COMPOSE_NAV_DESTINATIONS_IMPORT.toClassName())
            addStatement("init(listOf($specNames))")
        }
    }


//        return FileContentInfo(
//            fileImportInfo = PackageUtils.COMPOSE_NAV_DESTINATIONS_EXTENSION_FILE_IMPORT,
//            imports = mutableSetOf<ImportInfo>().apply {
//                add(PackageUtils.COMPOSE_NAV_DESTINATIONS_IMPORT)
//                rootNavGraphs.forEach {
//                    add(it.specImport)
//                }
//            },
//            code = CodeTemplates.NAV_COMPOSE_NAV_DESTINATIONS_EXTENSIONS_TEMPLATE.replace(
//                PLACEHOLDER_ROOT_NAV_GRAPH_SPECS_ARGUMENT, rootNavGraphs.joinToString(",") {
//                    it.simpleName
//                }
//            )
//        )
}