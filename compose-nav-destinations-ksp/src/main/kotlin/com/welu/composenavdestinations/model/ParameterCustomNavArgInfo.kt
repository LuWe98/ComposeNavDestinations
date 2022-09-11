package com.welu.composenavdestinations.model

data class ParameterCustomNavArgInfo(
    val parameterTypeImport: ImportInfo,
    val navArgTypeImport: ImportInfo
) {
    val allImports get(): List<ImportInfo> = listOf(parameterTypeImport, navArgTypeImport)
}