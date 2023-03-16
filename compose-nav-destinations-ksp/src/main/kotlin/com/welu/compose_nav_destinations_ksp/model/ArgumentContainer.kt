package com.welu.compose_nav_destinations_ksp.model

import com.welu.compose_nav_destinations_ksp.utils.ImportUtils

enum class ArgumentContainer(
    val variableName: String,
    val import: ImportInfo
) {
    NabBackStackEntry(
        variableName = "navBackStackEntry",
        import = ImportUtils.ANDROID_NAVIGATION_NAV_BACK_STACK_ENTRY_IMPORT
    ),
    SaveStateHandle(
        variableName = "savedStateHandle",
        import = ImportUtils.SAVED_STATE_HANDLE_IMPORT
    )
}