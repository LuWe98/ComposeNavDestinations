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
import com.welu.compose_nav_destinations_ksp.generation.FileSpecGenerator
import com.welu.compose_nav_destinations_ksp.model.ArgumentContainer
import com.welu.compose_nav_destinations_ksp.model.ImportInfo
import com.welu.compose_nav_destinations_ksp.model.components.ComposeDestinationInfo
import com.welu.compose_nav_destinations_ksp.model.components.ComposeNavGraphInfo
import com.welu.compose_nav_destinations_ksp.utils.ImportUtils
import com.welu.compose_nav_destinations_ksp.generation.component.NavComponentGeneratorUtils as Utils

object FileGeneratorDestinationSpecNew : FileSpecGenerator<ComposeDestinationInfo> {

    override fun generate(input: ComposeDestinationInfo) = FileSpec.build(input.specImport) {
        generateNavGraphObject(input).let(::addType)
        addImports(input.imports)
        addImport(ImportUtils.NAV_ARGUMENT_IMPORT)
    }

    private fun generateNavGraphObject(input: ComposeDestinationInfo) = TypeSpec.buildObject(input.simpleName) {
        Utils.generateBaseRouteProperty(input.baseRoute).let(::addProperty)
        Utils.generateParentNavGraphProperty(input.parentNavGraphSpecImport).let(::addProperty)
        generateDestinationProperty(input.destinationImport).let(::addProperty)

        if (input.deepLinks.any()) {
            //TODO -> Generate DeepLink Implementations
        }

        if (input.navArgsInfo == null) {
            addSuperinterface(input.destinationType.specImportInfo.toClassName())
            return@buildObject
        }

        addSuperinterface(input.destinationType.specImportInfo.toClassName().parameterizedBy(input.navArgsInfo.typeInfo.type.import))

        val (requiredParameters, optionalParameters) = input.navArgsInfo.partitionedParameters

        Utils.generateGetArgsFromFunction(
            component = input,
            containerType = ArgumentContainer.NabBackStackEntry
        ).let(::addFunction)

        Utils.generateGetArgsFromFunction(
            component = input,
            containerType = ArgumentContainer.SaveStateHandle
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

    private fun generateDestinationProperty(
        destinationImport: ImportInfo
    ) = PropertySpec.build(
        name = "destination",
        typeName = destinationImport.toClassName(),
        modifiers = arrayOf(OVERRIDE)
    ) {
        getter {
            addStatement("return %T", destinationImport.toClassName())
        }
    }
}