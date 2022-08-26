package com.welu.composenavdestinations.generation

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Resolver
import com.welu.composenavdestinations.extensions.ksp.dependencies
import com.welu.composenavdestinations.extensions.write
import com.welu.composenavdestinations.mapper.ParameterTypeMapper
import com.welu.composenavdestinations.model.NavDestinationInfo

class NavDestinationFileGenerator(
    private val resolver: Resolver,
    private val codeGenerator: CodeGenerator
): FileGenerator<NavDestinationInfo> {

//    val coreTypes = mapOf(
//        String::class.asType() to CORE_STRING_NAV_TYPE,
//        Int::class.asType() to CORE_INT_NAV_TYPE,
//        Float::class.asType() to CORE_FLOAT_NAV_TYPE,
//        Long::class.asType() to CORE_LONG_NAV_TYPE,
//        Boolean::class.asType() to CORE_BOOLEAN_NAV_TYPE,
//
//        IntArray::class.asType() to CORE_INT_ARRAY_NAV_TYPE,
//        FloatArray::class.asType() to CORE_FLOAT_ARRAY_NAV_TYPE,
//        LongArray::class.asType() to CORE_LONG_ARRAY_NAV_TYPE,
//        BooleanArray::class.asType() to CORE_BOOLEAN_ARRAY_NAV_TYPE,
//        Array::class.asTypeWithArg(String::class) to CORE_STRING_ARRAY_NAV_TYPE,
//
//        ArrayList::class.asTypeWithArg(Boolean::class) to CORE_BOOLEAN_ARRAY_LIST_NAV_TYPE,
//        ArrayList::class.asTypeWithArg(Float::class) to CORE_FLOAT_ARRAY_LIST_NAV_TYPE,
//        ArrayList::class.asTypeWithArg(Int::class) to CORE_INT_ARRAY_LIST_NAV_TYPE,
//        ArrayList::class.asTypeWithArg(Long::class) to CORE_LONG_ARRAY_LIST_NAV_TYPE,
//        ArrayList::class.asTypeWithArg(String::class) to CORE_STRING_ARRAY_LIST_NAV_TYPE,
//    )

    override fun generate(instance: NavDestinationInfo) {
        val fileOutputStream = codeGenerator.createNewFile(
            dependencies = resolver.dependencies,
            packageName = instance.packageName,
            fileName = instance.name
        )

        fileOutputStream.write("package ${instance.packageName}\n\n")

        instance.allImports.forEach {
            fileOutputStream.write(it.asImportLine + "\n")
        }

        fileOutputStream.write("\n")

//        fileOutputStream.write("DESTINATION NAME: ${instance.name}\n")
//        fileOutputStream.write("DESTINATION ROUTE NAME: ${instance.route}\n")


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
//
        instance.parameters.forEach { ParameterTypeMapper.map(it) }

        fileOutputStream.close()
    }

}