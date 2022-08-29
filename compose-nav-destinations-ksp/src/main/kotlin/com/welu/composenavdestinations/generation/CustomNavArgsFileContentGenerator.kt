package com.welu.composenavdestinations.generation

import com.welu.composenavdestinations.model.FileContentInfo
import com.welu.composenavdestinations.model.NavDestinationInfo
import com.welu.composenavdestinations.model.Parameter
import com.welu.composenavdestinations.utils.PackageUtils

class CustomNavArgsFileContentGenerator(
    private val fileOutputWriter: FileOutputWriter
) : FileContentGenerator<Sequence<NavDestinationInfo>> {

    override fun generate(instance: Sequence<NavDestinationInfo>) {
        val parameters: Sequence<Parameter> = extractCustomNavArgParameters(instance)
        if (parameters.none()) return
        fileOutputWriter.writeFile(generateFileContent(parameters))
    }

    private fun generateFileContent(parameters: Sequence<Parameter>) = FileContentInfo(
        packageDir = PackageUtils.NAV_ARGS_PACKAGE,
        imports = parameters.flatMap { it.navArgInfo.customNavArgInfo!!.allImports }.toSet(),
        code = parameters.joinToString("\n") {
            val navArgTypeName = it.navArgInfo.customNavArgInfo!!.navArgTypeImport.simpleName
            val parameterTypeName = it.navArgInfo.customNavArgInfo.parameterTypeImport.simpleName
            "val ${it.navArgInfo.import.simpleName} = $navArgTypeName($parameterTypeName::class)"
        },
        fileName = PackageUtils.CUSTOM_NAV_ARGS_FILE
    )

    private fun extractCustomNavArgParameters(navDestinationInfos: Sequence<NavDestinationInfo>) = navDestinationInfos.flatMap { info ->
        info.parameters.filter { it.navArgInfo.customNavArgInfo != null }
    }.distinctBy {
        it.navArgInfo.import.simpleName
    }
}