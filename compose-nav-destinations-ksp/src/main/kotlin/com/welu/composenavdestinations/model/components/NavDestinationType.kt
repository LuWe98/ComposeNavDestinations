package com.welu.composenavdestinations.model.components

import com.google.devtools.ksp.symbol.KSType
import com.welu.composenavdestinations.model.ImportInfo
import com.welu.composenavdestinations.utils.PackageUtils

enum class NavDestinationType(
    val specImportInfo: ImportInfo
) {
    Destination(PackageUtils.NAV_DESTINATION_SPEC_IMPORT),
    ArgDestination(PackageUtils.NAV_DESTINATION_ARG_SPEC_IMPORT),
    DialogDestination(PackageUtils.NAV_DIALOG_DESTINATION_SPEC_IMPORT),
    DialogArgDestination(PackageUtils.NAV_DIALOG_ARG_DESTINATION_SPEC_IMPORT),
    BottomSheetDestination(PackageUtils.NAV_BOTTOM_SHEET_DESTINATION_SPEC_IMPORT),
    BottomSheetArgDestination(PackageUtils.NAV_BOTTOM_SHEET_ARG_DESTINATION_SPEC_IMPORT);

    companion object {
        fun fromDestinationSuperType(destinationClassSupertype: KSType) = fromDestinationQualifiedName(
            destinationQualifiedName = destinationClassSupertype.declaration.qualifiedName?.asString()!!
        )

        private fun fromDestinationQualifiedName(destinationQualifiedName: String): NavDestinationType = when (destinationQualifiedName) {
            PackageUtils.NAV_DESTINATION_IMPORT.qualifiedName -> Destination
            PackageUtils.NAV_ARG_DESTINATION_IMPORT.qualifiedName -> ArgDestination
            PackageUtils.NAV_DIALOG_DESTINATION_IMPORT.qualifiedName -> DialogDestination
            PackageUtils.NAV_DIALOG_ARG_DESTINATION_IMPORT.qualifiedName -> DialogArgDestination
            PackageUtils.NAV_BOTTOM_SHEET_DESTINATION_IMPORT.qualifiedName -> BottomSheetDestination
            PackageUtils.NAV_BOTTOM_SHEET_ARG_DESTINATION_IMPORT.qualifiedName -> BottomSheetArgDestination
            else -> throw IllegalArgumentException("Destination does not implement a valid ComposeDestination interface: $destinationQualifiedName")
        }
    }
}