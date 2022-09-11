package com.welu.composenavdestinations.model

import com.welu.composenavdestinations.extensions.isNoneOf
import com.welu.composenavdestinations.utils.PackageUtils

data class ImportInfo(
    val simpleName: String,
    val packageDir: String,
    val importedAs: String? = null
) {

    constructor(qualifiedName: String, importedAs: String? = null): this(getSimpleName(qualifiedName), getRoot(qualifiedName), importedAs)

    val qualifiedName get() = "$packageDir.$simpleName"
    val isWholePackageImport get() = simpleName == "*"
    val isNonDefaultPackage get() = packageDir.isNoneOf(*PackageUtils.KOTLIN_DEFAULT_PACKAGES)
    val asImportLine get() = "import $qualifiedName${importedAs?.let { " as $it" } ?: ""}"

    companion object {
        val FILE_IMPORT_REGEX = Regex("(import)\\s+\\w+(\\s*\\.\\s*\\w+)*(\\s*\\.\\s*(\\w+|\\*))?\\s*(as\\s+\\w*)?")

        private fun getRoot(qualifiedName: String): String = qualifiedName.substring(0, qualifiedName.indexOfLast { it == '.' })

        private fun getSimpleName(qualifiedName: String): String = qualifiedName.substring(qualifiedName.indexOfLast{ it == '.' } + 1)

        fun fromImportLine(importLine: String): ImportInfo {
            val importDeclaration = importLine.substring(6).replace("\\s*\\.\\s*".toRegex(),".")
            val indexOfAlias = importDeclaration.indexOf(" as ")
            val importedAs = if(indexOfAlias == -1) null else importDeclaration.substring(indexOfAlias + 4)
            val qualifiedName = if(indexOfAlias == -1) importDeclaration else importDeclaration.substring(0, indexOfAlias).trim()
            return ImportInfo(qualifiedName.trim(), importedAs)
        }
    }

}