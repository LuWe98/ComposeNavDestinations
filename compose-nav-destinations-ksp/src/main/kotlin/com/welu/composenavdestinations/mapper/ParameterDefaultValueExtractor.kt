package com.welu.composenavdestinations.mapper

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.FileLocation
import com.google.devtools.ksp.symbol.KSValueParameter
import com.welu.composenavdestinations.extensions.*
import com.welu.composenavdestinations.extensions.ksp.isImportNameContainedInPackage
import com.welu.composenavdestinations.model.ParameterDefaultValue
import com.welu.composenavdestinations.model.ParameterDefaultValueDeclaration
import com.welu.composenavdestinations.model.KSFileContent
import com.welu.composenavdestinations.model.ImportInfo

object ParameterDefaultValueExtractor {

    private const val NO_DOT_DIGIT_CRITERIA = "(\\d(\\d|_)*)?"
    private const val DOT_DIGIT_CRITERIA = "(\\d((\\d|_)*\\d)?)?\\.(\\d(\\d|_)*)?"
    private const val ENDS_WITH_CRITERIA = "\\d[d|f|F]?"
    private val NUMBER_REGEX = Regex("^(($NO_DOT_DIGIT_CRITERIA|$DOT_DIGIT_CRITERIA)$ENDS_WITH_CRITERIA)\$")

    fun KSValueParameter.extractDefaultValue(
        resolver: Resolver,
        fileContent: KSFileContent,
        argQualifiedType: String,
    ): ParameterDefaultValue? {


        if (!hasDefault) return null

        val argLineNumber = (location as FileLocation).lineNumber - 1
        val argType: String = type.toString()

        val anchorMatcher = Regex("${name!!.asString()}\\s*:\\s*($argType|$argQualifiedType)\\s*(<(,|<|>|\\w|\\s|\\*|\\?)+>\\s*)?\\??\\s*=.*")
        val accumulatedLineBuilder = StringBuilder("")

        for (index in argLineNumber until fileContent.lines.size) {
            accumulatedLineBuilder.append(fileContent.lines[index])
            val findResult = anchorMatcher.find(accumulatedLineBuilder) ?: continue
            val matchedRange = accumulatedLineBuilder.substring(findResult.range)
            val defaultValueString = findDefaultValueSubString(matchedRange, argType) ?: continue

            // Es handelt sich um Boolean, Null oder eine Zahl
            if (defaultValueString.isOneOf("true", "false", "null") || defaultValueString.matches(NUMBER_REGEX)) {
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

        val indexOfParameterDeclarationEnd = findIndexOfParameterDeclarationEnd(matchedRange)
        if (indexOfParameterDeclarationEnd == -1) return null

        val indexOfParameterDefaultValueDeclaration = matchedRange.indexOfFirst('=')
        val subMatchedRange = matchedRange.substring(indexOfParameterDefaultValueDeclaration + 1, indexOfParameterDeclarationEnd)

        if (subMatchedRange.isBlank()) return null
        return subMatchedRange.reformatCodeWhitespaces()
    }

    private fun findIndexOfParameterDeclarationEnd(matchedRange: String): Int {
        var isInsideChar = false
        var isInsideString = false
        var openCrockCounter = 0
        var openCurlyCounter = 0
        var currentChar: Char

        for (charIndex in matchedRange.indices) {
            currentChar = matchedRange[charIndex]

            if (currentChar.isOneOf(',', ')')
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

        return -1
    }

    private fun String.reformatCodeWhitespaces(): String {
        val reformatBuilder = StringBuilder()
        var isInsideString = false
        var currentChar: Char
        var lastChar: Char? = null

        for (charIndex in indices) {
            currentChar = get(charIndex)
            if (currentChar == '"' && (charIndex == 0 || get(charIndex - 1) != '\\')) {
                isInsideString = !isInsideString
                if (lastChar.isWhitespace()) {
                    reformatBuilder.append(lastChar)
                }
            } else if (!isInsideString && charIndex != 0) {
                if (currentChar.isWhitespace() && get(charIndex - 1).isOneOf(' ', '(', '<', '.')) {
                    continue
                }
                if (lastChar.isWhitespace() && currentChar.isNoneOf(',', ')', '>', '.', ' ')) {
                    reformatBuilder.append(lastChar)
                }
            }

            if (isInsideString || !currentChar.isWhitespace()) {
                reformatBuilder.append(currentChar)
            }
            lastChar = currentChar
        }

        return reformatBuilder.trim().toString()
    }

    //TODO -> Bei allen isInsideString muss man prüfen, ob $ oder ${} vorhanden ist, das sind dann auch Deklarationen
    // Operator Functions erkennen und adden
    private fun extractDefaultValueDeclarations(defaultValueString: String): List<ParameterDefaultValueDeclaration> {
        val declarationBuilder = StringBuilder()
        var isInsideString = false
        var currentChar: Char
        var lastSeparatorCharacter: Char? = null
        val defaultValueDeclarations = mutableListOf<ParameterDefaultValueDeclaration>()
        var futureCallingDeclaration: ParameterDefaultValueDeclaration? = null

        fun currentPotentialDeclaration(charIndex: Int) = ParameterDefaultValueDeclaration(
            name = declarationBuilder.toString(),
            range = IntRange(charIndex - declarationBuilder.length, charIndex),
            callingDeclaration = futureCallingDeclaration
        )

        for (charIndex in defaultValueString.indices) {
            currentChar = defaultValueString[charIndex]

            if (currentChar == '"' && (charIndex == 0 || defaultValueString[charIndex - 1] != '\\')) {
                if (!isInsideString && declarationBuilder.isNotEmpty()) {
                    currentPotentialDeclaration(charIndex).let { declaration ->
                        defaultValueDeclarations.add(declaration)
                        futureCallingDeclaration = null
                        declarationBuilder.clear()
                    }
                }
                isInsideString = !isInsideString
            }

            if (isInsideString){
                //Hier noch auf $ überprüfen -> Nach einem Dollarsign können Deklarationen kommen
                continue
            }

            if (currentChar.isLetterOrDigitOrUnderscore()) {
                declarationBuilder.append(currentChar)

                if (charIndex == defaultValueString.lastIndex && lastSeparatorCharacter == '.') {
                    defaultValueDeclarations.add(currentPotentialDeclaration(charIndex))
                }
                continue
            }

            // Um Parameternamen zu filtern
            if(currentChar == '='){
                declarationBuilder.clear()
                continue
            }

            if (currentChar.isOneOf(',', '.', ':', '<', '>', '(', ')', '{', '}')) {
                lastSeparatorCharacter = currentChar

                if (declarationBuilder.isEmpty()) continue

                if (declarationBuilder.first().isDigit()) {
                    declarationBuilder.clear()
                    continue
                }

                currentPotentialDeclaration(charIndex).let { declaration ->
                    defaultValueDeclarations.add(declaration)
                    futureCallingDeclaration = if (currentChar == '.') declaration else null
                    declarationBuilder.clear()
                }
            }
        }

        return defaultValueDeclarations
    }

    private fun findRelevantImportsForDeclaration(
        declaration: ParameterDefaultValueDeclaration,
        fileImportInfos: List<ImportInfo>,
        resolver: Resolver
    ): List<ImportInfo> {
        val filtered = fileImportInfos.filter { declaration.name.isOneOf(it.simpleName, it.importedAs) }
        if (filtered.isNotEmpty()) return filtered

        return fileImportInfos.filter { import ->
            import.isWholePackageImport && resolver.isImportNameContainedInPackage(import.packageDir, declaration.name)
        }.map { import ->
            //TODO -> Überprüfen ob das Mapping sinnvoll ist oder nicht
            import.copy(simpleName = declaration.name)
        }
    }
}