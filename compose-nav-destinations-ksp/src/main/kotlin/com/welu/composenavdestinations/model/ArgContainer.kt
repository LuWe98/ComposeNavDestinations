package com.welu.composenavdestinations.model

enum class ArgContainer(
    val variableName: String
) {
    NabBackStackEntry("navBackStackEntry"),
    SaveStateHandle("savedStateHandle")
}