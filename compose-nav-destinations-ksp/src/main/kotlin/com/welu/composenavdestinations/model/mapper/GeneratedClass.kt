package com.welu.composenavdestinations.model.mapper

import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration

data class GeneratedClass(
    val routeName: String,
    val classDeclaration: KSClassDeclaration,
    val type: ClassKind,
    val parameters: List<ValueParameter> = emptyList()
) {
    val packageName get() = classDeclaration.packageName.asString()
    val simpleName get() = classDeclaration.simpleName.asString()
}