package com.welu.composenavdestinations.generation

import com.welu.composenavdestinations.model.FileContentInfo
import com.welu.composenavdestinations.model.ImportInfo
import com.welu.composenavdestinations.model.NavDestinationInfo
import com.welu.composenavdestinations.utils.PackageUtils

object FileGeneratorNavDestinationUtils : FileContentInfoGenerator<Sequence<NavDestinationInfo>> {

    override fun generate(instance: Sequence<NavDestinationInfo>): FileContentInfo = generateFileContent(instance)

    private fun generateFileContent(parameters: Sequence<NavDestinationInfo>) = FileContentInfo(
        fileImportInfo = PackageUtils.NAV_DESTINATION_UTILS_FILE_IMPORT,
        imports = mutableListOf<ImportInfo>().apply {
            addAll(parameters.map(NavDestinationInfo::destinationImport))
            addAll(parameters.map(NavDestinationInfo::destinationSpecImport))
            add(PackageUtils.NAV_DESTINATION_IMPORT)
            add(PackageUtils.NAV_DESTINATION_SPEC_IMPORT)
            add(PackageUtils.NAV_DESTINATION_SCOPE_IMPORT)
        },
        code = CodeTemplates.NAV_DESTINATION_UTILS_TEMPLATE
            .replace(CodeTemplates.PLACEHOLDER_NAV_UTILS_DESTINATION_SPEC_MAP, generateDestinationsSpecMapEntries(parameters))
    )

    private fun generateDestinationsSpecMapEntries(parameters: Sequence<NavDestinationInfo>): String =
        parameters.joinToString(",\n\t\t") {
            it.destinationImport.simpleName + " to " + it.destinationSpecImport.simpleName
        }

}