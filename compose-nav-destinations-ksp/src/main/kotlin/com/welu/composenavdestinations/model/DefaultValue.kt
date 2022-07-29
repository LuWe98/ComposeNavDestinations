package com.welu.composenavdestinations.model

data class DefaultValue(
    val value: String,
    val imports: List<PackageImport> = emptyList()
)