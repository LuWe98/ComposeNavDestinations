package com.welu.compose_nav_destinations_ksp.extensions.ksp

import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.welu.compose_nav_destinations_ksp.model.ImportInfo

fun KSDeclaration.asClassImport(simpleNameSuffix: String = ""): ImportInfo = ImportInfo(simpleName.asString() + simpleNameSuffix.trim(), packageName.asString())

val KSClassDeclaration.asType get(): KSType = asType(emptyList())

val KSClassDeclaration?.isEnum get() = this?.classKind == ClassKind.ENUM_CLASS

fun KSClassDeclaration.isParameterPresent(parameterName: String) = primaryConstructor?.parameters?.any { parameter ->
    parameter.name?.asString() == parameterName
} ?: false