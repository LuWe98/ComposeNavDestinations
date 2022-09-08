package com.welu.composenavdestinations.generation

import com.welu.composenavdestinations.model.FileContentInfo
import com.welu.composenavdestinations.model.NavDestinationInfo
import com.welu.composenavdestinations.utils.PackageUtils

object FileGeneratorNavDestinationUtils : FileContentInfoGenerator<Sequence<NavDestinationInfo>> {

    override fun generate(instance: Sequence<NavDestinationInfo>): FileContentInfo = generateFileContent(instance)

    private fun generateFileContent(parameters: Sequence<NavDestinationInfo>) = FileContentInfo(
        fileName = PackageUtils.NAV_DESTINATION_UTILS_FILE_NAME,
        packageDir = PackageUtils.NAV_ARGS_UTILS_PACKAGE,
        imports = parameters.map(NavDestinationInfo::import).toMutableList().apply {
            add(PackageUtils.NAV_DESTINATION_SPEC_IMPORT)
        },
        code = CodeTemplates.NAV_DESTINATION_UTILS_TEMPLATE
            .replace(CodeTemplates.PLACEHOLDER_NAV_UTILS_ALL_DESTINATIONS, generateAllNavDestinationsListEntries(parameters))
    )

    private fun generateAllNavDestinationsListEntries(parameters: Sequence<NavDestinationInfo>): String =
        parameters.joinToString(",\n\t\t", transform = NavDestinationInfo::simpleName)

}