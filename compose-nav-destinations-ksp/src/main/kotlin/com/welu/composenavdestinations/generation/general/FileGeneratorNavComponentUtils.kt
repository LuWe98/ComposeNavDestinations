package com.welu.composenavdestinations.generation.general

import com.welu.composenavdestinations.generation.FileContentInfoGenerator
import com.welu.composenavdestinations.generation.templates.GeneralCodeTemplates
import com.welu.composenavdestinations.model.FileContentInfo
import com.welu.composenavdestinations.model.ImportInfo
import com.welu.composenavdestinations.model.components.NavDestinationInfo
import com.welu.composenavdestinations.utils.PackageUtils

object FileGeneratorNavComponentUtils : FileContentInfoGenerator<Sequence<NavDestinationInfo>> {

    override fun generate(instance: Sequence<NavDestinationInfo>): FileContentInfo = generateFileContent(instance)

    private fun generateFileContent(parameters: Sequence<NavDestinationInfo>) = FileContentInfo(
        fileImportInfo = PackageUtils.NAV_COMPONENT_UTILS_FILE_IMPORT,
        imports = mutableListOf<ImportInfo>().apply {
            addAll(parameters.map(NavDestinationInfo::destinationImport))
            addAll(parameters.map(NavDestinationInfo::specImport))
            add(PackageUtils.NAV_DESTINATION_IMPORT)
            add(PackageUtils.NAV_DESTINATION_SPEC_IMPORT)
            add(PackageUtils.NAV_DESTINATION_SCOPE_IMPORT)
        },
        code = GeneralCodeTemplates.NAV_COMPONENT_UTILS_TEMPLATE
            .replace(GeneralCodeTemplates.PLACEHOLDER_NAV_UTILS_DESTINATION_SPEC_MAP, generateDestinationsSpecMapEntries(parameters))
    )

    private fun generateDestinationsSpecMapEntries(parameters: Sequence<NavDestinationInfo>): String =
        parameters.joinToString(",\n\t\t") {
            it.destinationImport.simpleName + " to " + it.specImport.simpleName
        }

}