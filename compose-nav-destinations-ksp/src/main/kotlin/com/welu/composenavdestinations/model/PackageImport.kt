package com.welu.composenavdestinations.model

import com.welu.composenavdestinations.extensions.isOneOf
import com.welu.composenavdestinations.utils.StandardLibraries

data class PackageImport(
    val simpleName: String,
    val root: String,
    val importedAs: String? = null
) {

    constructor(simpleName: String, qualifiedName: String): this(simpleName, getRoot(simpleName, qualifiedName), null)

    val qualifiedName get() = "$root.$simpleName"
    val isWholePackageImport get() = simpleName == "*"
    val isDefaultPackage get(): Boolean = root.isOneOf(*StandardLibraries.KOTLIN_DEFAULT)
    val isNonDefaultPackage get() = !isDefaultPackage
    val asImportLine get() = "import $qualifiedName${importedAs?.let { " as $it" } ?: ""}"

    fun withSimpleName(otherSimpleName: String) = copy(simpleName = otherSimpleName)

    companion object {
        val FILE_IMPORT_REGEX = Regex("(import)\\s+\\w+(\\s*\\.\\s*\\w+)*(\\s*\\.\\s*(\\w+|\\*))?\\s*(as\\s+\\w*)?")

        private fun getRoot(simpleName: String, qualifiedName: String) = qualifiedName.substring(0, qualifiedName.length - simpleName.length - 1)

        fun fromImportLine(importLine: String): PackageImport {
            val qualifiedNameWithImportedAs = importLine.substring(6) // Strips IMPORT
            val indexOfAliasImport = qualifiedNameWithImportedAs.indexOf(" as ")
            val qualifiedName = (if(indexOfAliasImport != -1) qualifiedNameWithImportedAs.substring(0, indexOfAliasImport) else qualifiedNameWithImportedAs)
                .replace(Regex("\\s*\\.\\s*"),".")
                .trim()
            val simpleName = qualifiedName.substring(qualifiedName.indexOfLast { it == '.' } + 1)
            val importedAs = if(indexOfAliasImport == -1) null else qualifiedNameWithImportedAs.substring(indexOfAliasImport + 4).trim()
            return PackageImport(
                simpleName = simpleName,
                root = getRoot(simpleName, qualifiedName),
                importedAs = importedAs
            )
        }
    }
}
