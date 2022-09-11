package com.welu.composenavdestinations.model

data class FileContentInfo(
    val fileName: String,
    val packageDir: String,
    val code: String,
    val imports: Iterable<ImportInfo>
) {
    constructor(fileImportInfo: ImportInfo, code: String, imports: Iterable<ImportInfo>): this(
        fileName = fileImportInfo.simpleName,
        packageDir = fileImportInfo.packageDir,
        code = code,
        imports = imports
    )
}