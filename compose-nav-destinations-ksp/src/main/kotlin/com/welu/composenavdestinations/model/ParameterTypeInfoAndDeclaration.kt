package com.welu.composenavdestinations.model

import com.google.devtools.ksp.symbol.KSClassDeclaration

data class ParameterTypeInfoAndDeclaration(
    val typeInfo: ParameterTypeInfo,
    val classDeclaration: KSClassDeclaration
)
