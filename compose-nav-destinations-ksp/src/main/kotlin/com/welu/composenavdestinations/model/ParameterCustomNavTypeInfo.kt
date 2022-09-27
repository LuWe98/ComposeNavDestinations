package com.welu.composenavdestinations.model

data class ParameterCustomNavTypeInfo(
    val parameterTypeImport: ImportInfo,
    val generatedCustomNavArgTypeImport: ImportInfo
) {
    //val imports get(): List<ImportInfo> = listOf(parameterTypeImport, generatedCustomNavArgTypeImport)
}