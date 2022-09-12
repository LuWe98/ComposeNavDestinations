package com.welu.composenavdestinations.extensions.ksp

import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType

val KSClassDeclaration.asType get(): KSType = asType(emptyList())

val KSClassDeclaration?.isEnum get() = this?.classKind == ClassKind.ENUM_CLASS

