package com.welu.composenavdestinations.generation.general

import com.welu.composenavdestinations.generation.FileContentInfoTypedGenerator
import com.welu.composenavdestinations.model.*
import com.welu.composenavdestinations.model.components.NavComponentInfo
import com.welu.composenavdestinations.utils.PackageUtils

object FileGeneratorCustomNavArgs : FileContentInfoTypedGenerator<Sequence<NavComponentInfo>> {

    override fun generate(instance: Sequence<NavComponentInfo>): FileContentInfo? = extractCustomNavArgParameters(instance)
        .takeIf(Sequence<Parameter>::any)
        ?.let(::generateFileContent)

    private fun extractCustomNavArgParameters(navComponents: Sequence<NavComponentInfo>) = navComponents
        .filter { it.navArgsInfo != null }
        .flatMap { it.navArgsInfo!!.parameters.filter(Parameter::hasCustomNavArgType) }
        .distinctBy { (it.navArgTypeInfo.navArgType as CustomNavArgType).generatedNavArgImport.simpleName }

    //            (Parameter::navArgTypeInfo / ParameterNavTypeInfo::customNavTypeInfo / ParameterCustomNavTypeInfo::)

    private fun generateFileContent(parameters: Sequence<Parameter>) = FileContentInfo(
        fileImportInfo = PackageUtils.NAV_DESTINATION_CUSTOM_NAV_ARGS_FILE_IMPORT,
        imports = mutableSetOf<ImportInfo>().apply {
            addAll(parameters.map { it.navArgTypeInfo.customNavTypeInfo!!.parameterTypeImport })
            addAll(parameters.map { it.navArgTypeInfo.navArgType.importInfo })
        },
        code = parameters.joinToString("\n") {
            val generatedCustomNavArgName = it.navArgTypeInfo.customNavTypeInfo!!.generatedCustomNavArgTypeImport.simpleName
            val navArgType = it.navArgTypeInfo.simpleName
            val navArgParameterType = it.navArgTypeInfo.customNavTypeInfo.parameterTypeImport.simpleName
            "val $generatedCustomNavArgName = $navArgType($navArgParameterType::class)"
        }
    )

}