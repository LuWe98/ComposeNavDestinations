package com.welu.compose_nav_destinations_ksp.generation.general

import com.welu.compose_nav_destinations_ksp.generation.FileContentInfoGenerator
import com.welu.compose_nav_destinations_ksp.generation.templates.CodeTemplates
import com.welu.compose_nav_destinations_ksp.model.FileContentInfo
import com.welu.compose_nav_destinations_ksp.utils.PackageUtils

object FileGeneratorNavBackStackEntryExtensions: FileContentInfoGenerator {

    override fun generate() = FileContentInfo(
        fileImportInfo = PackageUtils.NAV_BACK_STACK_ENTRY_EXTENSIONS_FILE_IMPORT,
        imports = mutableSetOf(
            PackageUtils.ANDROID_NAVIGATION_NAV_BACK_STACK_ENTRY_IMPORT,
            PackageUtils.NAV_COMPONENT_UTILS_FILE_IMPORT,
            PackageUtils.NAV_ROUTABLE_COMPOSE_DESTINATION_IMPORT
        ),
        code = CodeTemplates.NAV_BACK_STACK_ENTRY_EXTENSIONS_TEMPLATE
    )

}