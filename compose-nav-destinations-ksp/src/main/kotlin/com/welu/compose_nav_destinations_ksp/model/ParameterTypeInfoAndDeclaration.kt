package com.welu.compose_nav_destinations_ksp.model

import com.google.devtools.ksp.symbol.KSClassDeclaration

data class ParameterTypeInfoAndDeclaration(
    val typeInfo: ParameterTypeInfo,
    val classDeclaration: KSClassDeclaration
)
