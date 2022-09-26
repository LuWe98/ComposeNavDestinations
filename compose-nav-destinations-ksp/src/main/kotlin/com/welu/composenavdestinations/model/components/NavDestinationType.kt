package com.welu.composenavdestinations.model.components

import com.google.devtools.ksp.symbol.KSType
import com.welu.composenavdestinations.model.ImportInfo
import com.welu.composenavdestinations.utils.PackageUtils

enum class NavDestinationType(
    val specImportInfo: ImportInfo
) {
    DESTINATION(PackageUtils.NAV_DESTINATION_SPEC_IMPORT),
    ARG_DESTINATION(PackageUtils.NAV_DESTINATION_ARG_SPEC_IMPORT),
    DIALOG_DESTINATION(PackageUtils.NAV_DIALOG_DESTINATION_SPEC_IMPORT),
    DIALOG_ARG_DESTINATION(PackageUtils.NAV_DIALOG_ARG_DESTINATION_SPEC_IMPORT),
    BOTTOM_SHEET_DESTINATION(PackageUtils.NAV_BOTTOM_SHEET_DESTINATION_SPEC_IMPORT),
    BOTTOM_SHEET_ARG_DESTINATION(PackageUtils.NAV_BOTTOM_SHEET_ARG_DESTINATION_SPEC_IMPORT);

    companion object {
        fun fromDestinationSuperType(destinationClassSupertype: KSType) = fromDestinationQualifiedName(
            destinationQualifiedName = destinationClassSupertype.declaration.qualifiedName?.asString()!!
        )

        private fun fromDestinationQualifiedName(destinationQualifiedName: String): NavDestinationType = when (destinationQualifiedName) {
            PackageUtils.NAV_DESTINATION_IMPORT.qualifiedName -> DESTINATION
            PackageUtils.NAV_ARG_DESTINATION_IMPORT.qualifiedName -> ARG_DESTINATION
            PackageUtils.NAV_DIALOG_DESTINATION_IMPORT.qualifiedName -> DIALOG_DESTINATION
            PackageUtils.NAV_DIALOG_ARG_DESTINATION_IMPORT.qualifiedName -> DIALOG_ARG_DESTINATION
            PackageUtils.NAV_BOTTOM_SHEET_DESTINATION_IMPORT.qualifiedName -> BOTTOM_SHEET_DESTINATION
            PackageUtils.NAV_BOTTOM_SHEET_ARG_DESTINATION_IMPORT.qualifiedName -> BOTTOM_SHEET_ARG_DESTINATION
            else -> throw IllegalArgumentException("Destination does not implement a valid ComposeDestination interface: $destinationQualifiedName")
        }
    }
}