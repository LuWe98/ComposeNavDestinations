package com.welu.compose_nav_destinations_ksp.generation.general

import com.welu.compose_nav_destinations_ksp.generation.FileContentInfoTypedGenerator
import com.welu.compose_nav_destinations_ksp.model.FileContentInfo
import com.welu.compose_nav_destinations_ksp.model.ImportInfo
import com.welu.compose_nav_destinations_ksp.model.Parameter
import com.welu.compose_nav_destinations_ksp.model.components.NavComponentInfo
import com.welu.compose_nav_destinations_ksp.model.navargs.ComplexParameterNavArgType
import com.welu.compose_nav_destinations_ksp.utils.PackageUtils

object FileGeneratorCustomNavArgs : FileContentInfoTypedGenerator<Sequence<NavComponentInfo>> {

    override fun generate(instance: Sequence<NavComponentInfo>): FileContentInfo? = extractCustomNavArgParameters(instance)
        .takeIf(Sequence<Parameter>::any)
        ?.let(::generateFileContent)

    private fun extractCustomNavArgParameters(navComponents: Sequence<NavComponentInfo>) = navComponents
        .filter { it.navArgsInfo != null }
        .flatMap { it.navArgsInfo!!.parameters.filter(Parameter::hasComplexNavArgType) }
        .distinctBy { (it.navArgType as ComplexParameterNavArgType).generatedNavArgImport }

    private fun generateFileContent(parameters: Sequence<Parameter>) = FileContentInfo(
        fileImportInfo = PackageUtils.NAV_DESTINATION_CUSTOM_NAV_ARGS_FILE_IMPORT,
        imports = mutableSetOf<ImportInfo>().apply {
            addAll(parameters.map { (it.navArgType as ComplexParameterNavArgType).parameterTypeImport })
            addAll(parameters.map { it.navArgType.importInfo })
        },
        code = parameters.joinToString("\n") {
            val generatedCustomNavArgName = (it.navArgType as ComplexParameterNavArgType).generatedNavArgImport.simpleName
            val navArgTypeName = it.navArgType.simpleName
            val navArgParameterTypeName = it.navArgType.parameterTypeImport.simpleName
            "val $generatedCustomNavArgName = $navArgTypeName($navArgParameterTypeName::class)"
        }
    )

}