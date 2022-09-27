package com.welu.composenavdestinations.model

data class ParameterCustomNavTypeInfo(
    val parameterTypeImport: ImportInfo,
    val navArgType: CustomNavArgType
) {
    val imports get(): List<ImportInfo> = listOf(parameterTypeImport, navArgType.importInfo)
}