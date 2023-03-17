package com.welu.compose_nav_destinations_ksp.generation.component

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier.OVERRIDE
import com.squareup.kotlinpoet.LIST
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.welu.compose_nav_destinations_ksp.extensions.kotlinpoet.addImport
import com.welu.compose_nav_destinations_ksp.extensions.kotlinpoet.addImports
import com.welu.compose_nav_destinations_ksp.extensions.kotlinpoet.build
import com.welu.compose_nav_destinations_ksp.extensions.kotlinpoet.buildObject
import com.welu.compose_nav_destinations_ksp.extensions.kotlinpoet.getter
import com.welu.compose_nav_destinations_ksp.extensions.kotlinpoet.parameterizedBy
import com.welu.compose_nav_destinations_ksp.extensions.toClassName
import com.welu.compose_nav_destinations_ksp.generation.FileSpecMapper
import com.welu.compose_nav_destinations_ksp.model.ArgumentContainer
import com.welu.compose_nav_destinations_ksp.model.ImportInfo
import com.welu.compose_nav_destinations_ksp.model.components.ComposeNavGraphInfo
import com.welu.compose_nav_destinations_ksp.utils.ImportUtils
import com.welu.compose_nav_destinations_ksp.generation.component.NavComponentGeneratorUtils as Utils

object NavGraphSpecFileMapper : FileSpecMapper<ComposeNavGraphInfo> {

    override fun generate(input: ComposeNavGraphInfo) = FileSpec.build(input.specImport) {
        generateNavGraphObject(input).let(::addType)
        addImports(input.imports)
        addImport(ImportUtils.NAV_ARGUMENT_IMPORT)
    }

    private fun generateNavGraphObject(input: ComposeNavGraphInfo) = TypeSpec.buildObject(input.simpleName) {
        Utils.generateBaseRouteProperty(input.baseRoute).let(::addProperty)
        generateStartComponentProperty(input.startComponentDeclaration).let(::addProperty)
        generateChildNavComponentsProperty(input.allChildNavComponentSpecImports).let(::addProperty)

        if (!input.isRoot) {
            Utils.generateParentNavGraphProperty(input.parentNavGraphSpecImport!!).let(::addProperty)
        }

        if (input.deepLinks.any()) {
            //TODO -> Generate DeepLink Implementations
        }

        if (input.navArgsInfo == null) {
            addSuperinterface(ImportUtils.NAV_GRAPH_SPEC_IMPORT.toClassName())
            return@buildObject
        }

        addSuperinterface(ImportUtils.NAV_GRAPH_SPEC_ARG_IMPORT.toClassName().parameterizedBy(input.navArgsInfo.typeInfo.type.import))

        val (requiredParameters, optionalParameters) = input.navArgsInfo.partitionedParameters

        Utils.generateGetArgsFromFunction(
            component = input,
            containerType = ArgumentContainer.NabBackStackEntry
        ).let(::addFunction)

        Utils.generateArgRouteProperty(
            requiredParameters = requiredParameters,
            optionalParameters = optionalParameters
        ).let(::addProperty)

        Utils.generateNavArgumentsProperty(
            requiredParameters = requiredParameters,
            optionalParameters = optionalParameters
        ).let(::addProperty)

        Utils.generateInvokeFunction(
            baseRoute = input.baseRoute,
            requiredParameters = requiredParameters,
            optionalParameters = optionalParameters
        ).let(::addFunction)
    }

    private fun generateStartComponentProperty(startComponentImport: ImportInfo) = PropertySpec.build(
        name = "startComponentSpec",
        typeName = startComponentImport.toClassName(),
        modifiers = arrayOf(OVERRIDE)
    ) {
        getter {
            addStatement("return %T", startComponentImport.toClassName())
        }
    }

    private fun generateChildNavComponentsProperty(childComponentImports: List<ImportInfo>) = PropertySpec.build(
        name = "childNavComponentSpecs",
        typeName = LIST.parameterizedBy(ImportUtils.NAV_COMPONENT_SPEC_IMPORT),
        modifiers = arrayOf(OVERRIDE)
    ) {
        getter {
            val placeHolders = childComponentImports.joinToString(",") { "%T" }
            val specClassNames = childComponentImports.map { it.toClassName() }.toList().toTypedArray()
            addStatement("return listOf($placeHolders)", *specClassNames)
        }
    }
}