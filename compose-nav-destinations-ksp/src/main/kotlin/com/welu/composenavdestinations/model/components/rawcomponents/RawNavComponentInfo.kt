package com.welu.composenavdestinations.model.components.rawcomponents

import com.google.devtools.ksp.symbol.KSClassDeclaration

sealed interface RawNavComponentInfo {
    val isStart: Boolean
    val classDeclaration: KSClassDeclaration
    val baseRoute: String
}