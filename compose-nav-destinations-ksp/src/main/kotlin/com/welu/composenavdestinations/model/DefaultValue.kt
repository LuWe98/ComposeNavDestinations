package com.welu.composenavdestinations.model

data class DefaultValue(
    val value: String,
    val requiredImports: List<PackageImport> = emptyList()
)