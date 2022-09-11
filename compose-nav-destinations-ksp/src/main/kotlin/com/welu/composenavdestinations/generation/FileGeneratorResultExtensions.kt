package com.welu.composenavdestinations.generation

import com.welu.composenavdestinations.model.FileContentInfo
import com.welu.composenavdestinations.model.ImportInfo
import com.welu.composenavdestinations.model.NavDestinationInfo
import com.welu.composenavdestinations.utils.PackageUtils

object FileGeneratorResultExtensions : FileContentInfoGenerator<Sequence<NavDestinationInfo>> {

    override fun generate(instance: Sequence<NavDestinationInfo>): FileContentInfo {
        return FileContentInfo(
            fileImportInfo = PackageUtils.NAV_DESTINATION_RESULT_EXTENSIONS_FILE_IMPORT,
            imports = mutableSetOf<ImportInfo>().apply {
                add(PackageUtils.ANDROID_NAVIGATION_NAV_CONTROLLER_IMPORT)
                add(PackageUtils.ANDROID_COMPOSABLE_IMPORT)
                add(PackageUtils.ANDROID_LIVECYCLE_IMPORT)
                add(PackageUtils.NAV_DESTINATION_IMPORT)
                add(PackageUtils.NAV_DESTINATION_SPEC_IMPORT)
                add(PackageUtils.NAV_DESTINATION_SCOPE_IMPORT)
                add(PackageUtils.NAV_DESTINATION_SEND_DESTINATION_RESULT_FUNCTION_IMPORT)
                add(PackageUtils.NAV_DESTINATION_RESULT_LISTENER_IMPORT)
                add(PackageUtils.NAV_DESTINATION_LIFECYCLE_RESULT_LISTENER_IMPORT)
            },
            code = CodeTemplates.NAV_DESTINATION_RESULT_EXTENSIONS_TEMPLATE
        )
    }
}
