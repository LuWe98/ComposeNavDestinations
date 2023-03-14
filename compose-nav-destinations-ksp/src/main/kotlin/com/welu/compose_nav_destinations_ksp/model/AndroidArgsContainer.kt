package com.welu.compose_nav_destinations_ksp.model

import com.welu.compose_nav_destinations_ksp.utils.PackageUtils

enum class AndroidArgsContainer(
    val variableName: String,
    val import: ImportInfo
) {
    NabBackStackEntry("navBackStackEntry", PackageUtils.ANDROID_NAVIGATION_NAV_BACK_STACK_ENTRY_IMPORT),
    SaveStateHandle("savedStateHandle", PackageUtils.SAVED_STATE_HANDLE_IMPORT)
}