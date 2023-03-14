package com.welu.compose_nav_destinations_ksp.generation.extensions

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.welu.compose_nav_destinations_ksp.extensions.kotlinpoet.build
import com.welu.compose_nav_destinations_ksp.extensions.toClassName
import com.welu.compose_nav_destinations_ksp.generation.FileSpecGenerator
import com.welu.compose_nav_destinations_ksp.model.ImportInfo
import com.welu.compose_nav_destinations_ksp.model.components.NavComponentInfo
import com.welu.compose_nav_destinations_ksp.model.navargs.ComplexParameterNavArgType
import com.welu.compose_nav_destinations_ksp.utils.PackageUtils

object FileGeneratorCustomNavArgs : FileSpecGenerator<Sequence<NavComponentInfo>> {

    private val FILE_IMPORT = ImportInfo("NavDestinationCustomNavArgs", PackageUtils.NAV_ARGS_PACKAGE)

    override fun generate(input: Sequence<NavComponentInfo>): FileSpec? {
        val complexNavArgTypes = extractComplexNavArgTypes(input)

        if (complexNavArgTypes.none()) return null

        return FileSpec.build(FILE_IMPORT) {
            complexNavArgTypes.map(::generateCustomNavArgParameter).forEach {
                addProperty(it)
            }
        }
    }

    private fun extractComplexNavArgTypes(navComponents: Sequence<NavComponentInfo>): Sequence<ComplexParameterNavArgType> = navComponents
        .filter { it.navArgsInfo != null }
        .flatMap {
            it.navArgsInfo!!.parameters.mapNotNull { parameter ->
                parameter.navArgType as? ComplexParameterNavArgType
            }
        }.distinctBy { it.generatedNavArgImport }

    private fun generateCustomNavArgParameter(complexNavArgType: ComplexParameterNavArgType): PropertySpec {
        val customNavArgsTypeName = complexNavArgType.parameterTypeImport.toClassName()
        val typeName = complexNavArgType.importInfo.toClassName().parameterizedBy(customNavArgsTypeName)

        return PropertySpec.build(complexNavArgType.generatedNavArgImport.simpleName, typeName) {
            initializer(format = "${complexNavArgType.simpleName}(%T::class)", customNavArgsTypeName)
        }
    }
}