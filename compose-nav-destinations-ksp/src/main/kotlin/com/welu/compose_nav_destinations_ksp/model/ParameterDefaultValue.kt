package com.welu.compose_nav_destinations_ksp.model

data class ParameterDefaultValue(
    val value: String,
    val requiredImports: List<ImportInfo> = emptyList()
)