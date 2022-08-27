package com.welu.composenavdestinations.model

import com.welu.composenavdestinations.extensions.isOneOf
import com.welu.composenavdestinations.utils.StandardLibraries

data class PackageImportInfo(
    val simpleName: String,
    val root: String,
    val importedAs: String? = null
) {

    constructor(qualifiedName: String, importedAs: String? = null): this(getSimpleName(qualifiedName), getRoot(qualifiedName), importedAs)

    val qualifiedName get() = "$root.$simpleName"
    val isWholePackageImport get() = simpleName == "*"
    val isDefaultPackage get(): Boolean = root.isOneOf(*StandardLibraries.KOTLIN_DEFAULT)
    val isNonDefaultPackage get() = !isDefaultPackage
    val asImportLine get() = "import $qualifiedName${importedAs?.let { " as $it" } ?: ""}"

    fun withSimpleName(otherSimpleName: String) = copy(simpleName = otherSimpleName)

    companion object {
        val FILE_IMPORT_REGEX = Regex("(import)\\s+\\w+(\\s*\\.\\s*\\w+)*(\\s*\\.\\s*(\\w+|\\*))?\\s*(as\\s+\\w*)?")

        private fun getRoot(qualifiedName: String): String = qualifiedName.substring(0, qualifiedName.indexOfLast { it == '.' })

        private fun getSimpleName(qualifiedName: String): String = qualifiedName.substring(qualifiedName.indexOfLast{ it == '.' } + 1)

        fun fromImportLine(importLine: String): PackageImportInfo {
            val importDeclaration = importLine.substring(6).replace("\\s*\\.\\s*".toRegex(),".")
            val indexOfAlias = importDeclaration.indexOf(" as ")
            val importedAs = if(indexOfAlias == -1) null else importDeclaration.substring(indexOfAlias + 4)
            val qualifiedName = if(indexOfAlias == -1) importDeclaration else importDeclaration.substring(0, indexOfAlias)
            return PackageImportInfo(
                qualifiedName= qualifiedName.trim(),
                importedAs = importedAs?.trim()
            )
        }
    }
}
