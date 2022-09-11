package com.welu.composenavdestinations.model

data class ParameterDefaultValue(
    val value: String,
    val requiredImports: List<ImportInfo> = emptyList()
)