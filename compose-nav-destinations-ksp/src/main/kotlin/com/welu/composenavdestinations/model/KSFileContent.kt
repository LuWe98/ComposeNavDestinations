package com.welu.composenavdestinations.model

data class KSFileContent(
    val lines: List<String> = emptyList(),
    val packageImports: List<PackageImport> = emptyList()
)