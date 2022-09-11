package com.welu.composenavdestinations.generation

import com.welu.composenavdestinations.model.FileContentInfo
import com.welu.composenavdestinations.model.NavDestinationInfo
import com.welu.composenavdestinations.model.Parameter
import com.welu.composenavdestinations.utils.PackageUtils

object FileGeneratorCustomNavArgs : FileContentInfoGenerator<Sequence<NavDestinationInfo>> {

    override fun generate(instance: Sequence<NavDestinationInfo>): FileContentInfo? = extractCustomNavArgParameters(instance)
        .takeIf(Sequence<Parameter>::any)
        ?.let(::generateFileContent)

    private fun generateFileContent(parameters: Sequence<Parameter>) = FileContentInfo(
        fileImportInfo = PackageUtils.NAV_DESTINATION_CUSTOM_NAV_ARGS_FILE_IMPORT,
        imports = parameters.flatMap { it.navArgInfo.customNavArgInfo!!.allImports }.toSet(),
        code = parameters.joinToString("\n") {
            val navArgTypeName = it.navArgInfo.customNavArgInfo!!.navArgTypeImport.simpleName
            val parameterTypeName = it.navArgInfo.customNavArgInfo.parameterTypeImport.simpleName
            "val ${it.navArgInfo.import.simpleName} = $navArgTypeName($parameterTypeName::class)"
        }
    )

    private fun extractCustomNavArgParameters(navDestinationInfos: Sequence<NavDestinationInfo>) = navDestinationInfos.flatMap { info ->
        info.parameters.filter(Parameter::isCustomNavArgType)
    }.distinctBy {
        it.navArgInfo.import.simpleName
    }

}