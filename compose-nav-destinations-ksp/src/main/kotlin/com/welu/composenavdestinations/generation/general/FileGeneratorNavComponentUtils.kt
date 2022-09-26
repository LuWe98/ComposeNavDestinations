package com.welu.composenavdestinations.generation.general

import com.welu.composenavdestinations.generation.FileContentInfoTypedGenerator
import com.welu.composenavdestinations.generation.templates.CodeTemplates
import com.welu.composenavdestinations.model.FileContentInfo
import com.welu.composenavdestinations.model.components.NavComponentInfo
import com.welu.composenavdestinations.model.components.NavDestinationInfo
import com.welu.composenavdestinations.utils.PackageUtils

object FileGeneratorNavComponentUtils : FileContentInfoTypedGenerator<Sequence<NavComponentInfo>> {

    override fun generate(instance: Sequence<NavComponentInfo>): FileContentInfo {

        val destinationInfos = instance.filterIsInstance<NavDestinationInfo>()

        return FileContentInfo(
            fileImportInfo = PackageUtils.NAV_COMPONENT_UTILS_FILE_IMPORT,
            imports = mutableSetOf(
                PackageUtils.NAV_COMPOSE_SEALED_DESTINATION_IMPORT,
                PackageUtils.NAV_COMPOSE_DESTINATION_SPEC_IMPORT,
                PackageUtils.NAV_COMPOSE_DESTINATION_SCOPE_IMPORT,
                PackageUtils.NAV_COMPONENT_SPEC_IMPORT,
                PackageUtils.NAV_COMPOSE_DESTINATION_VAL_ROUTE_EXTENSION,
                PackageUtils.NAV_COMPOSE_GRAPH_SPEC_IMPORT
            ).apply {
                addAll(destinationInfos.map(NavDestinationInfo::destinationImport))
                addAll(instance.map(NavComponentInfo::specImport))
            },
            code = CodeTemplates.NAV_COMPONENT_UTILS_TEMPLATE
                .replace(
                    oldValue = CodeTemplates.PLACEHOLDER_NAV_UTILS_DESTINATION_SPEC_MAP,
                    newValue = generateDestinationsSpecMapEntries(destinationInfos)
                )
                .replace(
                    oldValue = CodeTemplates.PLACEHOLDER_NAV_UTILS_ALL_COMPONENT_SPECS_SET,
                    newValue = generateAllNavComponentSpecsSet(instance)
                )
        )
    }

    private fun generateAllNavComponentSpecsSet(componentSpecs: Sequence<NavComponentInfo>) = componentSpecs.joinToString(",\n\t\t") {
        it.specImport.simpleName
    }

    private fun generateDestinationsSpecMapEntries(destinations: Sequence<NavDestinationInfo>) = destinations.joinToString(",\n\t\t") {
        it.destinationImport.simpleName + " to " + it.specImport.simpleName
    }

}