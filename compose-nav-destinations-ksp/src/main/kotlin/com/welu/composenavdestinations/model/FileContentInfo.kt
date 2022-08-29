package com.welu.composenavdestinations.model

data class FileContentInfo(
    val fileName: String,
    val packageDir: String,
    val code: String,
    val imports: Iterable<ImportInfo>
)