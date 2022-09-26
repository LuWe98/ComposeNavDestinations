package com.welu.composenavdestinations.generation.general

import com.welu.composenavdestinations.generation.FileContentInfoGenerator
import com.welu.composenavdestinations.generation.templates.CodeTemplates
import com.welu.composenavdestinations.model.FileContentInfo
import com.welu.composenavdestinations.utils.PackageUtils

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