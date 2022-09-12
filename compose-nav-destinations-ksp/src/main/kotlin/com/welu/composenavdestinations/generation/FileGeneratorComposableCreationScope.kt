package com.welu.composenavdestinations.generation

import com.welu.composenavdestinations.model.FileContentInfo
import com.welu.composenavdestinations.model.ImportInfo
import com.welu.composenavdestinations.model.NavDestinationInfo
import com.welu.composenavdestinations.utils.PackageUtils

object FileGeneratorComposableCreationScope : FileContentInfoGenerator<Sequence<NavDestinationInfo>> {

    /**
     * -> Das ist für die Composable function. Das wird generiert und kann dann aufgerufen werden um die Composables zu initialisen
     */
    override fun generate(instance: Sequence<NavDestinationInfo>): FileContentInfo {

        val argDestinations = instance.filter(NavDestinationInfo::isArgDestination)

        return FileContentInfo(
            fileImportInfo = PackageUtils.NAV_DESTINATION_EXTENSIONS_FILE_IMPORT,
            imports = mutableSetOf<ImportInfo>().apply {

            },
            code = CodeTemplates.DESTINATION_EXT_TEMPLATE
        )
    }


}