package com.welu.composenavdestinations.generation

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Resolver
import com.welu.composenavdestinations.extensions.ksp.dependencies
import com.welu.composenavdestinations.extensions.write
import com.welu.composenavdestinations.extensions.writeLine
import com.welu.composenavdestinations.model.NavDestinationInfo
import com.welu.composenavdestinations.model.Parameter
import com.welu.composenavdestinations.utils.PackageUtils
import java.io.OutputStream

//Das ist die Datei, in welcher die custom NavArgs sind, damit man diese nur einmal erstellen muss für verschiedene Typen und die dann importieren kann
class NavDestinationCustomNavArgsFileGenerator(
    private val resolver: Resolver,
    private val codeGenerator: CodeGenerator
) : FileGenerator<Sequence<NavDestinationInfo>> {

    override fun generate(instance: Sequence<NavDestinationInfo>) {
        val customNavArgs: Sequence<Parameter> = extractCustomNavArgParameters(instance)
        if (!customNavArgs.iterator().hasNext()) return

        val fos = codeGenerator.createNewFile(
            dependencies = resolver.dependencies,
            packageName = PackageUtils.NAV_ARGS_PACKAGE,
            fileName = PackageUtils.CUSTOM_NAV_ARGS_FILE
        )

        fos.writeLine("package " + PackageUtils.NAV_ARGS_PACKAGE, 2)
        writeImports(fos, customNavArgs)
        writeCustomNavArgs(fos, customNavArgs)
        fos.close()
    }

    private fun extractCustomNavArgParameters(navDestinationInfos: Sequence<NavDestinationInfo>) = navDestinationInfos.flatMap { info ->
        info.parameters.filter { parameter ->
            parameter.navArgInfo.customNavArgInfo != null
        }
    }.distinctBy {
        it.navArgInfo.import.simpleName
    }

    private fun writeImports(fos: OutputStream, parameters: Sequence<Parameter>) {
        parameters.map {
            it.navArgInfo.customNavArgInfo!!.navArgTypeImport
        }.distinct().forEach {
            fos.writeLine(it.asImportLine)
        }
        parameters.map {
            it.navArgInfo.customNavArgInfo!!.parameterTypeImport
        }.distinct().forEach {
            fos.writeLine(it.asImportLine)
        }
        fos.write("\n")
    }

    private fun writeCustomNavArgs(fos: OutputStream, parameters: Sequence<Parameter>) = parameters.forEach {
        fos.writeLine("val ${it.navArgInfo.import.simpleName} = ${it.navArgInfo.customNavArgInfo!!.navArgTypeImport.simpleName}(${it.navArgInfo.customNavArgInfo.parameterTypeImport.simpleName}::class)")
    }
}