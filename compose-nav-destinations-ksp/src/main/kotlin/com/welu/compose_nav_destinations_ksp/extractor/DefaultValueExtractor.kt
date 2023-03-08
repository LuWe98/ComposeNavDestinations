package com.welu.compose_nav_destinations_ksp.extractor

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSValueParameter
import com.welu.compose_nav_destinations_ksp.extensions.indexOfFirst
import com.welu.compose_nav_destinations_ksp.extensions.isAnyOf
import com.welu.compose_nav_destinations_ksp.extensions.isLetterOrDigitOrUnderscore
import com.welu.compose_nav_destinations_ksp.extensions.isNoneOf
import com.welu.compose_nav_destinations_ksp.extensions.ksp.findFileLocation
import com.welu.compose_nav_destinations_ksp.extensions.ksp.isImportNameContainedInPackage
import com.welu.compose_nav_destinations_ksp.model.ImportInfo
import com.welu.compose_nav_destinations_ksp.model.KSFileContent
import com.welu.compose_nav_destinations_ksp.model.ParameterDefaultValue
import com.welu.compose_nav_destinations_ksp.model.ParameterDefaultValueDeclaration

class DefaultValueExtractor(
    private val resolver: Resolver
) {

    companion object {
        private const val NO_DOT_DIGIT_CRITERIA = "(\\d(\\d|_)*)?"
        private const val DOT_DIGIT_CRITERIA = "(\\d((\\d|_)*\\d)?)?\\.(\\d(\\d|_)*)?"
        private const val ENDS_WITH_CRITERIA = "\\d[d|f|F]?"
        private val NUMBER_REGEX = Regex("^(($NO_DOT_DIGIT_CRITERIA|$DOT_DIGIT_CRITERIA)$ENDS_WITH_CRITERIA)\$")
    }

    fun extract(
        parameter: KSValueParameter,
        argQualifiedName: String,
        fileContent: KSFileContent
    ): ParameterDefaultValue? {
        if (!parameter.hasDefault) return null

        val argLineNumber = parameter.findFileLocation()?.lineNumber?.minus(1) ?: 0
        val argType: String = parameter.type.toString()

        val anchorMatcher = Regex("${parameter.name!!.asString()}\\s*:\\s*($argType|$argQualifiedName)\\s*(<(,|<|>|\\w|\\s|\\*|\\?)+>\\s*)?\\??\\s*=.*")
        val accumulatedLineBuilder = StringBuilder("")

        for (index in argLineNumber until fileContent.lines.size) {
            accumulatedLineBuilder.append(fileContent.lines[index])
            val findResult = anchorMatcher.find(accumulatedLineBuilder) ?: continue
            val matchedRange = accumulatedLineBuilder.substring(findResult.range)
            val defaultValueString = findDefaultValueSubString(matchedRange, argType) ?: continue

            // Es handelt sich um Boolean, Null oder eine Zahl
            if (defaultValueString.isAnyOf("true", "false", "null") || defaultValueString.matches(NUMBER_REGEX)) {
                return ParameterDefaultValue(defaultValueString)
            }

            // Es handelt sich um ein Objekt, Funktion oder String.
            val imports = extractDefaultValueDeclarations(defaultValueString).flatMap {
                findRelevantImportsForDeclaration(it, fileContent.importInfos, resolver)
            }.distinctBy(ImportInfo::qualifiedName)

            return ParameterDefaultValue(defaultValueString, imports)
        }

        return null
    }

    private fun findDefaultValueSubString(parsedMatchedRange: String, argType: String): String? {
        val matchedRange = parsedMatchedRange.substring(parsedMatchedRange.indexOf(argType) + argType.length)

        val indexOfParameterDeclarationEnd = findIndexOfParameterDeclarationEnd(matchedRange) ?: return null
        val indexOfParameterDefaultValueDeclaration = matchedRange.indexOfFirst('=')

        val subMatchedRange = matchedRange.substring(indexOfParameterDefaultValueDeclaration + 1, indexOfParameterDeclarationEnd)
        if (subMatchedRange.isBlank()) return null

        return subMatchedRange.reformatCodeWhitespaces()
    }

    private fun findIndexOfParameterDeclarationEnd(matchedRange: String): Int? {
        var isInsideChar = false
        var isInsideString = false
        var openCrockCounter = 0
        var openCurlyCounter = 0

        matchedRange.forEachIndexed { charIndex, currentChar ->
            if (currentChar.isAnyOf(',', ')')
                && !isInsideChar
                && !isInsideString
                && openCrockCounter == 0
                && openCurlyCounter == 0
            ) return charIndex

            if (currentChar == '\'' && !isInsideString) {
                if (charIndex == 0 || matchedRange[charIndex - 1] != '\\') {
                    isInsideChar = !isInsideChar
                }
            } else if (currentChar == '"' && !isInsideChar) {
                if (charIndex == 0 || matchedRange[charIndex - 1] != '\\') {
                    isInsideString = !isInsideString
                }
            } else if (!isInsideString && !isInsideChar) {
                when (currentChar) {
                    '<' -> openCrockCounter++
                    '>' -> openCrockCounter--
                    '(' -> openCurlyCounter++
                    ')' -> openCurlyCounter--
                }
            }
        }

        return null
    }

    private fun String.reformatCodeWhitespaces(): String {
        val reformatBuilder = StringBuilder()
        var isInsideString = false
        var previousChar: Char? = null

        forEachIndexed { charIndex, currentChar ->
            if (currentChar == '"' && (charIndex == 0 || get(charIndex - 1) != '\\')) {
                isInsideString = !isInsideString
                if (previousChar?.isWhitespace() == true) {
                    reformatBuilder.append(previousChar)
                }
            } else if (!isInsideString && charIndex != 0) {
                if (currentChar.isWhitespace() && get(charIndex - 1).isAnyOf(' ', '(', '<', '.')) {
                    return@forEachIndexed
                }
                if (previousChar?.isWhitespace() == true && currentChar.isNoneOf(',', ')', '>', '.', ' ')) {
                    reformatBuilder.append(previousChar)
                }
            }

            if (isInsideString || !currentChar.isWhitespace()) {
                reformatBuilder.append(currentChar)
            }

            previousChar = currentChar
        }

        return reformatBuilder.trim().toString()
    }

    //TODO -> Bei allen isInsideString muss man prüfen, ob $ oder ${} vorhanden ist, das sind dann auch Deklarationen
    // Operator Functions erkennen und adden
    private fun extractDefaultValueDeclarations(defaultValueString: String): List<ParameterDefaultValueDeclaration> {
        val declarationBuilder = StringBuilder()
        var isInsideString = false
        var lastSeparatorCharacter: Char? = null
        val defaultValueDeclarations = mutableListOf<ParameterDefaultValueDeclaration>()
        var futureCallingDeclaration: ParameterDefaultValueDeclaration? = null

        fun currentPotentialDeclaration(charIndex: Int) = ParameterDefaultValueDeclaration(
            name = declarationBuilder.toString(),
            range = IntRange(charIndex - declarationBuilder.length, charIndex),
            callingDeclaration = futureCallingDeclaration
        )

        defaultValueString.forEachIndexed { charIndex, currentChar ->
            if (currentChar == '"' && (charIndex == 0 || defaultValueString[charIndex - 1] != '\\')) {
                if (!isInsideString && declarationBuilder.isNotEmpty()) {
                    val declaration = currentPotentialDeclaration(charIndex)
                    defaultValueDeclarations.add(declaration)
                    futureCallingDeclaration = null
                    declarationBuilder.clear()
                }
                isInsideString = !isInsideString
            }

            if (isInsideString) {
                //Hier noch auf $ überprüfen -> Nach einem Dollarsign können Deklarationen kommen
                return@forEachIndexed
            }

            if (currentChar.isLetterOrDigitOrUnderscore()) {
                declarationBuilder.append(currentChar)

                if (charIndex == defaultValueString.lastIndex && lastSeparatorCharacter == '.') {
                    defaultValueDeclarations.add(currentPotentialDeclaration(charIndex))
                }
                return@forEachIndexed
            }

            // Um Parameternamen zu filtern
            if (currentChar == '=') {
                declarationBuilder.clear()
                return@forEachIndexed
            }

            if (currentChar.isAnyOf(',', '.', ':', '<', '>', '(', ')', '{', '}')) {
                lastSeparatorCharacter = currentChar

                if (declarationBuilder.isEmpty()) return@forEachIndexed

                if (declarationBuilder.first().isDigit()) {
                    declarationBuilder.clear()
                    return@forEachIndexed
                }

                val declaration = currentPotentialDeclaration(charIndex)
                defaultValueDeclarations.add(declaration)
                futureCallingDeclaration = if (currentChar == '.') declaration else null
                declarationBuilder.clear()
            }
        }

        return defaultValueDeclarations
    }

    private fun findRelevantImportsForDeclaration(
        declaration: ParameterDefaultValueDeclaration,
        fileImportInfos: List<ImportInfo>,
        resolver: Resolver
    ): List<ImportInfo> {
        val filtered = fileImportInfos.filter { declaration.name.isAnyOf(it.simpleName, it.importedAs) }
        if (filtered.isNotEmpty()) return filtered

        return fileImportInfos.filter { import ->
            import.isWholePackageImport && resolver.isImportNameContainedInPackage(import.packageDir, declaration.name)
        }.map { import ->
            //TODO -> Überprüfen ob das Mapping sinnvoll ist oder nicht
            import.copy(simpleName = declaration.name)
        }
    }
}