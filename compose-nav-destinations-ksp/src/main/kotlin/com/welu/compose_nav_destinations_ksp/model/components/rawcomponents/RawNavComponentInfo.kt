package com.welu.compose_nav_destinations_ksp.model.components.rawcomponents

import com.google.devtools.ksp.symbol.KSClassDeclaration

sealed interface RawNavComponentInfo {
    val isStart: Boolean
    val classDeclaration: KSClassDeclaration
    val baseRoute: String
}