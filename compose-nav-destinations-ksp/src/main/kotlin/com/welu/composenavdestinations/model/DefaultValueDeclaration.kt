package com.welu.composenavdestinations.model

data class DefaultValueDeclaration(
    val name: String,
    val range: IntRange,
    val callingDeclaration: DefaultValueDeclaration? = null
)
