package com.welu.composenavdestinations.generation.component

import com.welu.composenavdestinations.generation.FileContentInfoTypedGenerator
import com.welu.composenavdestinations.generation.templates.CodeTemplates
import com.welu.composenavdestinations.model.FileContentInfo
import com.welu.composenavdestinations.model.Parameter
import com.welu.composenavdestinations.model.components.ComposeDestinationInfo
import com.welu.composenavdestinations.utils.PackageUtils

object FileGeneratorDestinationExtensions : FileContentInfoTypedGenerator<Sequence<ComposeDestinationInfo>> {

    override fun generate(instance: Sequence<ComposeDestinationInfo>): FileContentInfo {

        val argDestinations = instance.filter(ComposeDestinationInfo::isArgDestination)

        return FileContentInfo(
            fileImportInfo = PackageUtils.NAV_DESTINATION_EXTENSIONS_FILE_IMPORT,
            imports = mutableSetOf(
                PackageUtils.NAV_COMPONENT_UTILS_FILE_IMPORT,
                PackageUtils.NAV_COMPOSE_DESTINATION_SPEC_IMPORT,
                PackageUtils.NAV_COMPOSE_SEALED_DESTINATION_IMPORT,
                PackageUtils.NAV_ROUTABLE_COMPOSE_DESTINATION_SPEC_IMPORT,
                PackageUtils.NAV_ARG_COMPOSE_DESTINATION_SPEC_IMPORT,
                PackageUtils.NAV_ROUTABLE_COMPOSE_DESTINATION_IMPORT,
                PackageUtils.NAV_ARG_COMPOSE_DESTINATION_IMPORT
            ).apply {
                addAll(instance.map(ComposeDestinationInfo::destinationImport))
                addAll(instance.map(ComposeDestinationInfo::specImport))
                addAll(argDestinations.flatMap(ComposeDestinationInfo::typeImports))

                if (argDestinations.any()) {
                    add(PackageUtils.SAVED_STATE_HANDLE_IMPORT)
                    add(PackageUtils.ANDROID_NAVIGATION_NAV_BACK_STACK_ENTRY_IMPORT)
                }
            },
            code = CodeTemplates.DESTINATION_EXT_TEMPLATE
                .replace(
                    oldValue = CodeTemplates.PLACEHOLDER_DESTINATION_EXT_ARGS_FROM_METHODS,
                    newValue = generateArgsFromFunctions(argDestinations)
                ).replace(
                    oldValue = CodeTemplates.PLACEHOLDER_DESTINATION_EXT_ARGS_INVOKE_FUNCTION,
                    newValue = generateArgsInvokeFunction(argDestinations)
                )
        )
    }

    //TODO -> Für jede Destination eine eigene Extension File erstellen. Dadurch kommt es nicht zu problemen,
    // wenn man bsw. ein gleichnamiges Objekt in zwei unterschiedlichen Packages hat
    private fun generateArgsFromFunctions(argDestinations: Sequence<ComposeDestinationInfo>): String = argDestinations.joinToString("\n\n") {
        """
        | fun ${it.destinationImport.simpleName}.argsFrom(savedStateHandle: SavedStateHandle) = ${it.specImport.simpleName}.argsFrom(savedStateHandle)
        | 
        | fun ${it.destinationImport.simpleName}.argsFrom(navBackStackEntry: NavBackStackEntry) = ${it.specImport.simpleName}.argsFrom(navBackStackEntry)
        """.trimMargin("| ")
    }

    private fun generateArgsInvokeFunction(argDestinations: Sequence<ComposeDestinationInfo>): String = argDestinations.joinToString("\n\n") {
        """
        | operator fun ${it.destinationImport.simpleName}.invoke(
        |     ${it.navArgsInfo!!.parameters.joinToString(",\n\t\t| \t", transform = Parameter::fullDeclarationName)}
        | ) = ${it.specImport.simpleName}(
        |     ${it.navArgsInfo.parameters.joinToString(",\n\t\t| \t") { parameter -> "${parameter.name} = ${parameter.name}" }}
        | )
        """.trimMargin("| ")
    }
}
