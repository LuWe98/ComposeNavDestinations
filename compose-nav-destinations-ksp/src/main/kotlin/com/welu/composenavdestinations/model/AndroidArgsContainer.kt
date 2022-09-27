package com.welu.composenavdestinations.model

enum class AndroidArgsContainer(
    val variableName: String
) {
    NabBackStackEntry("navBackStackEntry"),
    SaveStateHandle("savedStateHandle")
}