package com.welu.compose_nav_destinations_ksp.generation.general

import com.welu.compose_nav_destinations_ksp.generation.FileContentInfoTypedGenerator
import com.welu.compose_nav_destinations_ksp.generation.templates.CodeTemplates
import com.welu.compose_nav_destinations_ksp.model.FileContentInfo
import com.welu.compose_nav_destinations_ksp.model.ImportInfo
import com.welu.compose_nav_destinations_ksp.model.components.ComposeDestinationInfo
import com.welu.compose_nav_destinations_ksp.utils.PackageUtils


object FileGeneratorResultExtensions : FileContentInfoTypedGenerator<Sequence<ComposeDestinationInfo>> {

    override fun generate(instance: Sequence<ComposeDestinationInfo>) = FileContentInfo(
        fileImportInfo = PackageUtils.COMPOSE_NAV_DESTINATIONS_EXTENSION_FILE_IMPORT,
        imports = mutableSetOf<ImportInfo>().apply {
            add(PackageUtils.ANDROID_NAVIGATION_NAV_CONTROLLER_IMPORT)
            add(PackageUtils.ANDROID_COMPOSABLE_IMPORT)
            add(PackageUtils.ANDROID_LIVECYCLE_IMPORT)
            add(PackageUtils.NAV_COMPOSE_SEALED_DESTINATION_IMPORT)
            add(PackageUtils.NAV_COMPOSE_DESTINATION_SPEC_IMPORT)
            add(PackageUtils.NAV_COMPOSE_DESTINATION_SCOPE_IMPORT)
            add(PackageUtils.NAV_DESTINATION_SEND_DESTINATION_RESULT_FUNCTION_IMPORT)
            add(PackageUtils.NAV_DESTINATION_RESULT_LISTENER_IMPORT)
            add(PackageUtils.NAV_DESTINATION_LIFECYCLE_RESULT_LISTENER_IMPORT)
            add(PackageUtils.COMPOSE_NAV_DESTINATIONS_IMPORT)
        },
        code = CodeTemplates.NAV_DESTINATION_RESULT_EXTENSIONS_TEMPLATE
    )

}
