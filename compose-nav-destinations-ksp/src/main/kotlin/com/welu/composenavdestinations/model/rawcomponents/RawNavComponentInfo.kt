package com.welu.composenavdestinations.model.rawcomponents

import com.google.devtools.ksp.symbol.KSClassDeclaration

sealed interface RawNavComponentInfo {
    val isStart: Boolean
    val classDeclaration: KSClassDeclaration
    val baseRoute: String
}