package com.welu.composenavdestinations.model

data class PackageImport(
    val simpleName: String,
    val root: String,
    val importedAs: String? = null
) {

    val qualifiedName get() = "$root.$simpleName"
    val isWholePackageImport get() = simpleName == "*"
    val asImportLine get() = "import $qualifiedName${importedAs?.let { " as $it" } ?: ""}"

    fun withSimpleName(otherSimpleName: String) = copy(simpleName = otherSimpleName)

    companion object {

        fun fromPackageImportMatch(result: MatchResult) = fromPackageImportLine(result.value)

        fun fromPackageImportLine(import: String): PackageImport {
            val qualifiedNameWithImportedAs = import.substring(6) // Strips IMPORT
            val indexOfAliasImport = qualifiedNameWithImportedAs.indexOf(" as ")
            val qualifiedName  = getQualifiedName(indexOfAliasImport, qualifiedNameWithImportedAs)
            val simpleName = getSimpleName(qualifiedName)
            val root = getRoot(simpleName, qualifiedName)
            val importedAs = if(indexOfAliasImport == -1) null else qualifiedNameWithImportedAs.substring(indexOfAliasImport + 4).trim() // Strips everything including " as "
            return PackageImport(simpleName, root, importedAs)
        }

        private fun getQualifiedName(importedAsIndex: Int, relevantRange: String) = (if(importedAsIndex != -1) relevantRange.substring(0, importedAsIndex) else relevantRange)
            .replace(Regex("\\s*\\.\\s*"),".")
            .trim()

        private fun getSimpleName(qualifiedName: String) = qualifiedName.substring(qualifiedName.indexOfLast { it == '.' } + 1)

        private fun getRoot(simpleName: String, qualifiedName: String) = qualifiedName.substring(0, qualifiedName.length - simpleName.length - 1)
    }
}