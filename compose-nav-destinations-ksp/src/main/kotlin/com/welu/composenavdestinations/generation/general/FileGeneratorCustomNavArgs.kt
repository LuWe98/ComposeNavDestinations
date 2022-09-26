package com.welu.composenavdestinations.generation.general

import com.welu.composenavdestinations.extensions.div
import com.welu.composenavdestinations.generation.FileContentInfoTypedGenerator
import com.welu.composenavdestinations.model.FileContentInfo
import com.welu.composenavdestinations.model.Parameter
import com.welu.composenavdestinations.model.ParameterNavTypeInfo
import com.welu.composenavdestinations.model.components.NavComponentInfo
import com.welu.composenavdestinations.utils.PackageUtils

object FileGeneratorCustomNavArgs : FileContentInfoTypedGenerator<Sequence<NavComponentInfo>> {

    override fun generate(instance: Sequence<NavComponentInfo>): FileContentInfo? = extractCustomNavArgParameters(instance)
        .takeIf(Sequence<Parameter>::any)
        ?.let(FileGeneratorCustomNavArgs::generateFileContent)

    private fun extractCustomNavArgParameters(navComponents: Sequence<NavComponentInfo>) = navComponents
        .filter { it.navArgsInfo != null }
        .flatMap { it.navArgsInfo!!.parameters.filter(Parameter::hasCustomNavArgType) }
        .distinctBy(Parameter::navArgTypeInfo / ParameterNavTypeInfo::simpleName)

    private fun generateFileContent(parameters: Sequence<Parameter>) = FileContentInfo(
        fileImportInfo = PackageUtils.NAV_DESTINATION_CUSTOM_NAV_ARGS_FILE_IMPORT,
        imports = parameters.flatMap { it.navArgTypeInfo.customNavTypeInfo!!.allImports }.toSet(),
        code = parameters.joinToString("\n") {
            val navArgTypeName = it.navArgTypeInfo.customNavTypeInfo!!.navArgTypeImport.simpleName
            val parameterTypeName = it.navArgTypeInfo.customNavTypeInfo.parameterTypeImport.simpleName
            "val ${it.navArgTypeInfo.simpleName} = $navArgTypeName($parameterTypeName::class)"
        }
    )

}