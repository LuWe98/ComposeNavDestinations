package com.welu.composenavdestinations.model

data class KSFileContent(
    val lines: List<String> = emptyList(),
    val importInfos: List<ImportInfo> = emptyList()
) {
    companion object {
        val EMPTY get() = KSFileContent()
    }
}