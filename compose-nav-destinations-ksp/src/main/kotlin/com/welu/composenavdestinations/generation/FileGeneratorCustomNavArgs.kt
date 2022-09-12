package com.welu.composenavdestinations.generation

import com.welu.composenavdestinations.extensions.div
import com.welu.composenavdestinations.model.FileContentInfo
import com.welu.composenavdestinations.model.NavDestinationInfo
import com.welu.composenavdestinations.model.Parameter
import com.welu.composenavdestinations.model.ParameterNavTypeInfo
import com.welu.composenavdestinations.utils.PackageUtils

object FileGeneratorCustomNavArgs : FileContentInfoGenerator<Sequence<NavDestinationInfo>> {

    override fun generate(instance: Sequence<NavDestinationInfo>): FileContentInfo? = extractCustomNavArgParameters(instance)
        .takeIf(Sequence<Parameter>::any)
        ?.let(::generateFileContent)

    private fun generateFileContent(parameters: Sequence<Parameter>) = FileContentInfo(
        fileImportInfo = PackageUtils.NAV_DESTINATION_CUSTOM_NAV_ARGS_FILE_IMPORT,
        imports = parameters.flatMap { it.navArgTypeInfo.customNavTypeInfo!!.allImports }.toSet(),
        code = parameters.joinToString("\n") {
            val navArgTypeName = it.navArgTypeInfo.customNavTypeInfo!!.navArgTypeImport.simpleName
            val parameterTypeName = it.navArgTypeInfo.customNavTypeInfo.parameterTypeImport.simpleName
            "val ${it.navArgTypeInfo.simpleName} = $navArgTypeName($parameterTypeName::class)"
        }
    )

    private fun extractCustomNavArgParameters(navDestinationInfos: Sequence<NavDestinationInfo>) = navDestinationInfos
        .filter(NavDestinationInfo::isArgDestination)
        .flatMap { it.navArgsInfo!!.parameters.filter(Parameter::hasCustomNavArgType) }
        .distinctBy(Parameter::navArgTypeInfo / ParameterNavTypeInfo::simpleName)

}