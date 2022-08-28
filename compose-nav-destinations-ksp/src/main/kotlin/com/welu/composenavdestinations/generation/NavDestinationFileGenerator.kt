package com.welu.composenavdestinations.generation

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Resolver
import com.welu.composenavdestinations.extensions.ksp.dependencies
import com.welu.composenavdestinations.extensions.write
import com.welu.composenavdestinations.extensions.writeComment
import com.welu.composenavdestinations.extensions.writeLine
import com.welu.composenavdestinations.mapper.ParameterNavArgInfoMapper
import com.welu.composenavdestinations.model.NavDestinationInfo
import com.welu.composenavdestinations.processors.NavDestinationsProcessor
import java.io.OutputStream

class NavDestinationFileGenerator(
    private val resolver: Resolver,
    private val codeGenerator: CodeGenerator
): FileGenerator<NavDestinationInfo> {

//        instance.parameters.forEach {
//            fileOutputStream.write("ArgName - ${it.name}, " +
//                    "Type - ${it.typeInfo.type.import.simpleName}, " +
//                    "IsNullable - ${it.typeInfo.isNullable}, " +
//                    "Def Value - ${it.defaultValue?.value ?: "[NO DEFAULT VALUE]"}\n\n")
//        }

    //        fileOutputStream.write("fun function(\n")
//        instance.parameters.forEach {
//            fileOutputStream.write("\t" + it.fullDeclarationName + ",\n")
//        }
//        fileOutputStream.write(") {}")


    //        instance.parameters.forEach { ParameterTypeMapper.map(it) }

    override fun generate(instance: NavDestinationInfo) {
        val fos = codeGenerator.createNewFile(
            dependencies = resolver.dependencies,
            packageName = instance.packageName,
            fileName = instance.name
        )

        fos.writeLine("package ${instance.packageName}", 2)
        //writeImports(fos, instance)

        instance.parameters.forEach {
            NavDestinationsProcessor.debugFile.writeComment("${it.navArgInfo}", 1)
        }

//        fos.writeLine("object ${instance.name} {", 2)
//        writeRouteArg(fos, instance)
//        writeNavArgsVariable(fos, instance)
//        fos.writeLine("}")

        fos.close()
    }

    private fun writeImports(fos: OutputStream, instance: NavDestinationInfo) {
//        fos.writeLine("import androidx.navigation.NamedNavArgument")
//        fos.writeLine("import com.welu.composenavdestinations.util.navArgument")
//        fos.writeLine("import com.welu.composenavdestinations.navargs.*")
        instance.allImports.forEach { fos.writeLine(it.asImportLine) }
        fos.write("\n")
    }

    private fun writeRouteArg(fos: OutputStream, instance: NavDestinationInfo) {
        fos.writeLine("""   val baseRoute: String = "${instance.route}" """, 2)
        fos.writeLine("""   val route: String = baseRoute${if(instance.parameters.isEmpty()) "" else " +"}""", 1)
        instance.parameters.joinToString("+\n") {
            "       \"/{${it.name}}\""
        }.also(fos::writeLine)
    }

    private fun writeNavArgsVariable(fos: OutputStream, instance: NavDestinationInfo){
        fos.writeLine("""   val arguments: List<NamedNavArgument> get() = listOf(""")
        instance.parameters.joinToString(",\n") {
            """         navArgument("${it.name}", NavArgLongType)"""
        }.also(fos::writeLine)
        fos.writeLine("""   )""")
    }


}