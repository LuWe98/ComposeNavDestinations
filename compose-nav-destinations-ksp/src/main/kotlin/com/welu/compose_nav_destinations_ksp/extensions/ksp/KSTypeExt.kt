package com.welu.compose_nav_destinations_ksp.extensions.ksp

import com.google.devtools.ksp.symbol.*

val KSType.isNullable get() = nullability == Nullability.NULLABLE

fun KSType.getTypeAlias(): KSType? = declaration.let { if(it is KSTypeAlias) it.type.resolve() else null }

fun KSType.getTypeAliasDeclaration(): KSDeclaration? = getTypeAlias()?.declaration

fun KSType.getTypeAliasClassDeclaration(): KSClassDeclaration = (getTypeAliasDeclaration() ?: declaration) as KSClassDeclaration


//fun KSDeclaration.getTypeAlias(): KSType? = if(this is KSTypeAlias) this.type.resolve() else null
//
//fun KSDeclaration.getTypeAliasDeclaration(): KSDeclaration? = getTypeAlias()?.declaration
