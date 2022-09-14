package com.welu.composenavdestinations.model.structure

import com.google.devtools.ksp.symbol.KSClassDeclaration

sealed interface RawComponentInfo {
    val isStart: Boolean
    val classDeclaration: KSClassDeclaration
}