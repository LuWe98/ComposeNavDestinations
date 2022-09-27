package com.welu.composenavdestinations.generation.general

import com.welu.composenavdestinations.generation.FileContentInfoTypedGenerator
import com.welu.composenavdestinations.model.*
import com.welu.composenavdestinations.model.components.NavComponentInfo
import com.welu.composenavdestinations.model.navargs.ComplexParameterNavArgType
import com.welu.composenavdestinations.utils.PackageUtils

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