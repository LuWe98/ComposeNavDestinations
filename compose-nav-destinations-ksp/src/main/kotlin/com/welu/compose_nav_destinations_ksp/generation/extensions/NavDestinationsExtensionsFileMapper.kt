package com.welu.compose_nav_destinations_ksp.generation.extensions

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.welu.compose_nav_destinations_ksp.extensions.kotlinpoet.build
import com.welu.compose_nav_destinations_ksp.extensions.toClassName
import com.welu.compose_nav_destinations_ksp.generation.FileSpecMapper
import com.welu.compose_nav_destinations_ksp.model.ImportInfo
import com.welu.compose_nav_destinations_ksp.model.components.ComposeNavGraphInfo
import com.welu.compose_nav_destinations_ksp.utils.ImportUtils
import com.welu.compose_nav_destinations_ksp.utils.ImportUtils.NAV_DESTINATIONS_EXTENSIONS_PACKAGE

object NavDestinationsExtensionsFileMapper : FileSpecMapper<Sequence<ComposeNavGraphInfo>> {

    private val FILE_IMPORT = ImportInfo("ComposeNavDestinationsExt", NAV_DESTINATIONS_EXTENSIONS_PACKAGE)

    override fun generate(input: Sequence<ComposeNavGraphInfo>): FileSpec? {
        val rootNavGraphs = input.filter(ComposeNavGraphInfo::isRoot)

        if (rootNavGraphs.none()) return null

        return FileSpec.build(FILE_IMPORT) {
            generateInitExtension(rootNavGraphs).let(::addFunction)
            //rootNavGraphs.map(ComposeNavGraphInfo::specImport).let(::addImports)
        }
    }

    private fun generateInitExtension(
        rootNavGraphs: Sequence<ComposeNavGraphInfo>
    ): FunSpec {
        val placeHolders = rootNavGraphs.joinToString(",") { "%T" }
        val specClassNames = rootNavGraphs.map { it.specImport.toClassName() }.toList().toTypedArray()

        return FunSpec.build("init") {
            receiver(ImportUtils.COMPOSE_NAV_DESTINATIONS_IMPORT.toClassName())
            addStatement("init(listOf($placeHolders))", *specClassNames)
        }
    }

// TODO -> Old variation
//    private fun generateInitExtension(
//        rootNavGraphs: Sequence<ComposeNavGraphInfo>
//    ): FunSpec {
//        val specNames = rootNavGraphs.joinToString(",", transform = ComposeNavGraphInfo::simpleName)
//        return FunSpec.build("init") {
//            receiver(PackageUtils.COMPOSE_NAV_DESTINATIONS_IMPORT.toClassName())
//            addStatement("init(listOf($specNames))")
//        }
//    }
}