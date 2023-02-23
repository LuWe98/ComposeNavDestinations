package com.welu.compose_nav_destinations_ksp.model

enum class AndroidArgsContainer(
    val variableName: String
) {
    NabBackStackEntry("navBackStackEntry"),
    SaveStateHandle("savedStateHandle")
}