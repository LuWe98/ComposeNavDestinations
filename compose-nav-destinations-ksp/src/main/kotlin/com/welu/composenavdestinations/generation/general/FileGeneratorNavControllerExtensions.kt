package com.welu.composenavdestinations.generation.general

import com.welu.composenavdestinations.generation.FileContentInfoGenerator
import com.welu.composenavdestinations.generation.templates.CodeTemplates
import com.welu.composenavdestinations.model.FileContentInfo
import com.welu.composenavdestinations.utils.PackageUtils

object FileGeneratorNavControllerExtensions: FileContentInfoGenerator {

    override fun generate() = FileContentInfo(
        fileImportInfo = PackageUtils.NAV_CONTROLLER_EXTENSIONS_FILE_IMPORT,
        imports = mutableSetOf(
            PackageUtils.ANDROID_NAVIGATION_NAV_CONTROLLER_IMPORT,
            PackageUtils.ANDROID_NAVIGATION_NAV_OPTIONS_BUILDER_IMPORT,
            PackageUtils.NAV_ROUTABLE_COMPOSE_DESTINATION_IMPORT
        ),
        code = CodeTemplates.NAV_CONTROLLER_EXTENSIONS_TEMPLATE
    )

}