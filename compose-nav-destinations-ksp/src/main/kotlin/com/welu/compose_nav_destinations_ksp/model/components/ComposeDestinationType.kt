package com.welu.compose_nav_destinations_ksp.model.components

import com.google.devtools.ksp.symbol.KSType
import com.welu.compose_nav_destinations_ksp.model.ImportInfo
import com.welu.compose_nav_destinations_ksp.utils.ImportUtils

enum class ComposeDestinationType(
    val specImportInfo: ImportInfo
) {
    Destination(ImportUtils.NAV_DESTINATION_SPEC_IMPORT),
    ArgDestination(ImportUtils.NAV_DESTINATION_ARG_SPEC_IMPORT),
    DialogDestination(ImportUtils.NAV_DIALOG_DESTINATION_SPEC_IMPORT),
    DialogArgDestination(ImportUtils.NAV_DIALOG_ARG_DESTINATION_SPEC_IMPORT),
    BottomSheetDestination(ImportUtils.NAV_BOTTOM_SHEET_DESTINATION_SPEC_IMPORT),
    BottomSheetArgDestination(ImportUtils.NAV_BOTTOM_SHEET_ARG_DESTINATION_SPEC_IMPORT);

    companion object {
        fun fromDestinationSuperType(destinationClassSupertype: KSType) = fromDestinationQualifiedName(
            destinationQualifiedName = destinationClassSupertype.declaration.qualifiedName?.asString()!!
        )

        private fun fromDestinationQualifiedName(destinationQualifiedName: String): ComposeDestinationType = when (destinationQualifiedName) {
            ImportUtils.NAV_DESTINATION_IMPORT.qualifiedName -> Destination
            ImportUtils.NAV_ARG_DESTINATION_IMPORT.qualifiedName -> ArgDestination
            ImportUtils.NAV_DIALOG_DESTINATION_IMPORT.qualifiedName -> DialogDestination
            ImportUtils.NAV_DIALOG_ARG_DESTINATION_IMPORT.qualifiedName -> DialogArgDestination
            ImportUtils.NAV_BOTTOM_SHEET_DESTINATION_IMPORT.qualifiedName -> BottomSheetDestination
            ImportUtils.NAV_BOTTOM_SHEET_ARG_DESTINATION_IMPORT.qualifiedName -> BottomSheetArgDestination
            else -> throw IllegalArgumentException("Destination does not implement a valid ComposeDestination interface: $destinationQualifiedName")
        }
    }
}