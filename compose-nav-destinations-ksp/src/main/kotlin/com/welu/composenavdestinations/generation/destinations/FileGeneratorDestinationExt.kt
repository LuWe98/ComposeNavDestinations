package com.welu.composenavdestinations.generation.destinations

import com.welu.composenavdestinations.generation.general.GeneralCodeTemplates
import com.welu.composenavdestinations.generation.FileContentInfoGenerator
import com.welu.composenavdestinations.model.FileContentInfo
import com.welu.composenavdestinations.model.ImportInfo
import com.welu.composenavdestinations.model.components.NavDestinationInfo
import com.welu.composenavdestinations.model.Parameter
import com.welu.composenavdestinations.utils.PackageUtils

object FileGeneratorDestinationExt : FileContentInfoGenerator<Sequence<NavDestinationInfo>> {

    override fun generate(instance: Sequence<NavDestinationInfo>): FileContentInfo {

        val argDestinations = instance.filter(NavDestinationInfo::isArgDestination)

        return FileContentInfo(
            fileImportInfo = PackageUtils.NAV_DESTINATION_EXTENSIONS_FILE_IMPORT,
            imports = mutableSetOf<ImportInfo>().apply {
                addAll(instance.map(NavDestinationInfo::destinationImport))
                addAll(instance.map(NavDestinationInfo::specImport))
                addAll(argDestinations.flatMap(NavDestinationInfo::allImports))

                add(PackageUtils.NAV_COMPONENT_UTILS_FILE_IMPORT)
                add(PackageUtils.NAV_DESTINATION_SPEC_IMPORT)
                add(PackageUtils.NAV_DESTINATION_PLAIN_SPEC_IMPORT)
                add(PackageUtils.NAV_DESTINATION_ARG_SPEC_IMPORT)
                add(PackageUtils.NAV_DESTINATION_IMPORT)
                add(PackageUtils.NAV_PLAIN_DESTINATION_IMPORT)
                add(PackageUtils.NAV_ARG_DESTINATION_IMPORT)
                add(PackageUtils.NAV_DESTINATION_SCOPE_IMPORT)
                add(PackageUtils.NAV_DESTINATION_SEND_DESTINATION_RESULT_FUNCTION_IMPORT)

                if (argDestinations.any()) {
                    add(PackageUtils.SAVED_STATE_HANDLE_IMPORT)
                    add(PackageUtils.ANDROID_NAVIGATION_NAV_BACK_STACK_ENTRY_IMPORT)
                }
            },
            code = GeneralCodeTemplates.DESTINATION_EXT_TEMPLATE
                .replace(GeneralCodeTemplates.PLACEHOLDER_DESTINATION_EXT_ARGS_FROM_METHODS, generateArgsFromFunctions(argDestinations))
                .replace(GeneralCodeTemplates.PLACEHOLDER_DESTINATION_EXT_ARGS_INVOKE_FUNCTION, generateArgsInvokeFunction(argDestinations))
        )
    }

    private fun generateArgsFromFunctions(argDestinations: Sequence<NavDestinationInfo>): String = argDestinations.joinToString("\n\n") {
        """
        | fun ${it.destinationImport.simpleName}.argsFrom(savedStateHandle: SavedStateHandle) = ${it.specImport.simpleName}.argsFrom(savedStateHandle)
        | 
        | fun ${it.destinationImport.simpleName}.argsFrom(navBackStackEntry: NavBackStackEntry) = ${it.specImport.simpleName}.argsFrom(navBackStackEntry)
        """.trimMargin("| ")
    }

    private fun generateArgsInvokeFunction(argDestinations: Sequence<NavDestinationInfo>): String = argDestinations.joinToString("\n\n") {
        """
        | operator fun ${it.destinationImport.simpleName}.invoke(
        |     ${it.navArgsInfo!!.parameters.joinToString(",\n\t\t| \t", transform = Parameter::fullDeclarationName)}
        | ) = ${it.specImport.simpleName}(
        |     ${it.navArgsInfo.parameters.joinToString(",\n\t\t| \t") { parameter -> "${parameter.name} = ${parameter.name}" }}
        | )
        """.trimMargin("| ")
    }
}
