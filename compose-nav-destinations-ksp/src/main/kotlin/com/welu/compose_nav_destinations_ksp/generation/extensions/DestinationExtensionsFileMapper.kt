package com.welu.compose_nav_destinations_ksp.generation.extensions

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.welu.compose_nav_destinations_ksp.extensions.kotlinpoet.addImports
import com.welu.compose_nav_destinations_ksp.extensions.kotlinpoet.build
import com.welu.compose_nav_destinations_ksp.extensions.toClassName
import com.welu.compose_nav_destinations_ksp.generation.FileSpecMapper
import com.welu.compose_nav_destinations_ksp.model.ArgumentContainer
import com.welu.compose_nav_destinations_ksp.model.ImportInfo
import com.welu.compose_nav_destinations_ksp.model.components.ComposeDestinationInfo
import com.welu.compose_nav_destinations_ksp.utils.ImportUtils

object DestinationExtensionsFileMapper : FileSpecMapper<Sequence<ComposeDestinationInfo>> {

    private val FILE_IMPORT = ImportInfo("NavDestinationExt", ImportUtils.NAV_DESTINATIONS_EXTENSIONS_PACKAGE)
    private const val ARGS_FROM_FUNCTION_NAME = "argsFrom"
    private const val INVOKE_FUNCTION_NAME = "invoke"

    override fun generate(input: Sequence<ComposeDestinationInfo>): FileSpec? {
        val argDestinations = input.filter(ComposeDestinationInfo::isArgDestination)
        if (argDestinations.none()) return null

        return FileSpec.build(FILE_IMPORT) {
            argDestinations.flatMap(ComposeDestinationInfo::typeImports).distinct().let(::addImports)

            argDestinations.forEach {
                generateArgsFromFunction(it, ArgumentContainer.SaveStateHandle).let(::addFunction)
                generateArgsFromFunction(it, ArgumentContainer.NabBackStackEntry).let(::addFunction)
                generateInvokeFunction(it).let(::addFunction)
            }
        }
    }

    private fun generateArgsFromFunction(
        argDestination: ComposeDestinationInfo,
        argsContainer: ArgumentContainer
    ) = FunSpec.build(ARGS_FROM_FUNCTION_NAME) {
        receiver(argDestination.destinationImport.toClassName())
        returns(argDestination.navArgsInfo!!.typeInfo.type.import.toClassName())
        addParameter(argsContainer.variableName, argsContainer.import.toClassName())
        addStatement("return %T.$ARGS_FROM_FUNCTION_NAME(${argsContainer.variableName})", argDestination.specImport.toClassName())
    }

    private fun generateInvokeFunction(argDestination: ComposeDestinationInfo) = FunSpec.build(INVOKE_FUNCTION_NAME) {
        receiver(argDestination.destinationImport.toClassName())
        returns(ImportUtils.ROUTABLE_IMPORT.toClassName())
        addModifiers(KModifier.OPERATOR)

        argDestination.navArgsInfo!!.parameters.forEach {
            ParameterSpec.build(it.name, it.typeInfo.toParameterizedTypeName()) {
                it.defaultValue?.value?.let(::defaultValue)
            }.let(::addParameter)
        }

        val params = argDestination.navArgsInfo.parameters.joinToString(",\n\t") { parameter ->
            "${parameter.name} = ${parameter.name}"
        }
        addStatement("return %T(\n\t$params\n)", argDestination.specImport.toClassName())
    }
}