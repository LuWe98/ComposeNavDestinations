package com.welu.compose_nav_destinations_lint.declarations

internal enum class DestinationDeclaration(override val packageDir: String = "com.welu.composenavdestinations.navigation.destinations"): Declaration {
    ComposeDestination,
    ComposeArgDestination,
    ComposeRoutableDestination,
    Destination,
    ArgDestination,
    DialogDestination,
    DialogArgDestination,
    BottomSheetDestination,
    BottomSheetArgDestination;
}