package com.welu.composenavdestinations.extensions.ksp

import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeAlias
import com.google.devtools.ksp.symbol.Nullability

val KSType.isNullable get() = nullability == Nullability.NULLABLE

fun KSType.getTypeAlias(): KSType? = declaration.let {
    if(it is KSTypeAlias) it.type.resolve() else null
}

fun KSType.getTypeAliasDeclaration(): KSDeclaration? = getTypeAlias()?.declaration


fun KSDeclaration.getTypeAlias(): KSType? = if(this is KSTypeAlias) this.type.resolve() else null

fun KSDeclaration.getTypeAliasDeclaration(): KSDeclaration? = getTypeAlias()?.declaration

