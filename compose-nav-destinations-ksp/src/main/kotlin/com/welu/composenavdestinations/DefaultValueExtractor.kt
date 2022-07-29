package com.welu.composenavdestinations

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.FileLocation
import com.google.devtools.ksp.symbol.KSValueParameter
import com.welu.composenavdestinations.DefaultValueExtractor.DefaultValueFindResult.Found
import com.welu.composenavdestinations.DefaultValueExtractor.DefaultValueFindResult.NotFound
import com.welu.composenavdestinations.extensions.*
import com.welu.composenavdestinations.model.DefaultValue
import com.welu.composenavdestinations.model.KSFileContent
import com.welu.composenavdestinations.model.PackageImport
import com.welu.composenavdestinations.model.DefaultValueDeclaration

object DefaultValueExtractor {

    //val qualifiedRegexToFind = Regex("^$argName\\s*:\\s*$argQualifiedType\\s*\\??\\s*=\\s*")

    private const val TRUE = "true"
    private const val FALSE = "false"
    private const val NULL = "null"

    private const val NO_DOT_DIGIT_CRITERIA = "(\\d(\\d|_)*)?"
    private const val DOT_DIGIT_CRITERIA = "(\\d((\\d|_)*\\d)?)?\\.(\\d(\\d|_)*)?"
    private const val ENDS_WITH_CRITERIA = "\\d[d|f|F]?"
    private val NUMBER_REGEX = Regex("^(($NO_DOT_DIGIT_CRITERIA|$DOT_DIGIT_CRITERIA)$ENDS_WITH_CRITERIA)\$")

    sealed class DefaultValueFindResult {
        class Found(val defaultValueString: String) : DefaultValueFindResult()
        object NotFound : DefaultValueFindResult()
    }

    fun getDefaultValue(
        parameter: KSValueParameter,
        resolver: Resolver,
        fileContent: KSFileContent,
        argQualifiedType: String
    ) = parameter.getDefaultValue(resolver, fileContent, argQualifiedType)

    @JvmName("getDefaultValueExt")
    fun KSValueParameter.getDefaultValue(
        resolver: Resolver,
        fileContent: KSFileContent,
        argQualifiedType: String,
    ): DefaultValue? {
        if (!hasDefault) return null

        val argLineNumber = (location as FileLocation).lineNumber - 1
        val argType: String = type.toString()

        val anchorMatcher = Regex("${name!!.asString()}\\s*:\\s*$argType\\s*\\??\\s*=.*")
        var accumulatedLines = ""

        for (index in argLineNumber until fileContent.lines.size) {
            accumulatedLines += fileContent.lines[index]

            val match = anchorMatcher.find(accumulatedLines) ?: continue
            val matchedRange = accumulatedLines.substring(match.range)

            val defaultValueString = when (val result = findDefaultValueSubString(matchedRange, argType)) {
                is Found -> result.defaultValueString
                NotFound -> continue
            }

            if (defaultValueString.isOneOf(TRUE, FALSE, NULL) || defaultValueString.matches(NUMBER_REGEX)) {
                return DefaultValue(defaultValueString)
            }

            // Es handelt sich um ein Objekt, Funktion oder String.
            val imports = extractDefaultValueDeclarations(defaultValueString).flatMap {
                findRelevantImportsForDeclaration(it, fileContent.packageImports, resolver)
            }.distinctBy(PackageImport::qualifiedName)

            return DefaultValue(defaultValueString, imports)
        }

        return null
    }

    private fun findDefaultValueSubString(parsedMatchedRange: String, argType: String): DefaultValueFindResult {
        val matchedRange = parsedMatchedRange.substring(parsedMatchedRange.indexOf(argType) + argType.length)

        val indexOfParameterDeclarationEnd = findIndexOfParameterDeclarationEnd(matchedRange)
        if (indexOfParameterDeclarationEnd == -1) return NotFound

        val indexOfParameterDefaultValueDeclaration = matchedRange.indexOfFirst { it == '=' }
        val subMatchedRange = matchedRange.substring(indexOfParameterDefaultValueDeclaration + 1, indexOfParameterDeclarationEnd)

        if (subMatchedRange.isBlank()) return NotFound
        return Found(subMatchedRange.reformatCodeWhitespaces().trim())
    }

    private fun findIndexOfParameterDeclarationEnd(matchedRange: String): Int {
        var isInsideChar = false
        var isInsideString = false
        var openCrockCounter = 0
        var openCurlyCounter = 0
        var char: Char

        for (charIndex in matchedRange.indices) {
            char = matchedRange[charIndex]

            if ((char == ',' || char == ')')
                && !isInsideChar
                && !isInsideString
                && openCrockCounter == 0
                && openCurlyCounter == 0
            ) return charIndex

            if (char == '\'' && !isInsideString) {
                if (charIndex == 0 || matchedRange[charIndex - 1] != '\\') {
                    isInsideChar = !isInsideChar
                }
            } else if (char == '"' && !isInsideChar) {
                if (charIndex == 0 || matchedRange[charIndex - 1] != '\\') {
                    isInsideString = !isInsideString
                }
            } else if (!isInsideString && !isInsideChar) {
                when (char) {
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
        var isInsideString = false
        var adjustedString = ""
        var currentChar: Char
        var lastChar: Char? = null

        for (charIndex in indices) {
            currentChar = get(charIndex)
            if (currentChar == '"' && (charIndex == 0 || get(charIndex - 1) != '\\')) {
                isInsideString = !isInsideString
                if (lastChar.isWhitespace()) {
                    adjustedString += lastChar
                }
            } else if (!isInsideString && charIndex != 0) {
                if (currentChar.isWhitespace() && get(charIndex - 1).isOneOf(' ', '(', '<', '.')) {
                    continue
                }
                if (lastChar.isWhitespace() && currentChar.isNoneOf(',', ')', '>', '.', ' ')) {
                    adjustedString += lastChar
                }
            }

            if (isInsideString || !currentChar.isWhitespace()) {
                adjustedString += currentChar
            }
            lastChar = currentChar
        }

        return adjustedString
    }

    //TODO -> Bei allsen isInsideString muss man prüfen, ob $ oder ${} vorhanden ist, das sind dann auch Deklarationen
    // Operator Functions erkennen und adden
    private fun extractDefaultValueDeclarations(defaultValueString: String): List<DefaultValueDeclaration> {
        var isInsideString = false
        var currentChar: Char
        var currentDeclaration = ""
        var lastSeparatorCharacter = '-'
        val defaultValueDeclarations = mutableListOf<DefaultValueDeclaration>()
        var futureCallingDeclaration: DefaultValueDeclaration? = null

        fun currentPotentialDeclaration(charIndex: Int) = DefaultValueDeclaration(
            name = currentDeclaration,
            range = IntRange(charIndex - currentDeclaration.length, charIndex),
            callingDeclaration = futureCallingDeclaration
        )

        for (charIndex in defaultValueString.indices) {
            currentChar = defaultValueString[charIndex]

            if (currentChar == '"' && (charIndex == 0 || defaultValueString[charIndex - 1] != '\\')) {
                if (!isInsideString && currentDeclaration.isNotEmpty()) {
                    currentPotentialDeclaration(charIndex).let { declaration ->
                        defaultValueDeclarations.add(declaration)
                        futureCallingDeclaration = null
                        currentDeclaration = ""
                    }
                }
                isInsideString = !isInsideString
            }

            if (isInsideString){
                //Hier dann auf $ überprüfen
                continue
            }

            if (currentChar.isLetterDigitOrUnderscore()) {
                currentDeclaration += currentChar

                if (charIndex == defaultValueString.lastIndex && lastSeparatorCharacter == '.') {
                    defaultValueDeclarations.add(currentPotentialDeclaration(charIndex))
                }
                continue
            }

            if (currentChar.isOneOf(',', '.', ':', '<', '>', '(', ')', '{', '}')) {
                lastSeparatorCharacter = currentChar

                if (currentDeclaration.isEmpty()) continue

                if (currentDeclaration.first().isDigit()) {
                    currentDeclaration = ""
                    continue
                }

                currentPotentialDeclaration(charIndex).let { declaration ->
                    defaultValueDeclarations.add(declaration)
                    futureCallingDeclaration = if (currentChar == '.') declaration else null
                    currentDeclaration = ""
                }
            }
        }

        return defaultValueDeclarations
    }

    private fun findRelevantImportsForDeclaration(
        declaration: DefaultValueDeclaration,
        filePackageImports: List<PackageImport>,
        resolver: Resolver
    ): List<PackageImport> {
        val filtered = filePackageImports.filter {
            declaration.name == it.simpleName
        }
        if (filtered.isNotEmpty()) return filtered

        return filePackageImports.filter {
            it.isWholePackageImport && resolver.isImportNameContainedInPackage(it.root, declaration.name)
        }.map {
            //TODO -> Überprüfen ob das Mapping Sinnvoll ist oder nicht
            it.withSimpleName(declaration.name)
        }
    }



//    private val STANDARD_DECLARATIONS = listOf(
//        "Byte",
//        "String",
//        "Char",
//        "Short",
//        "Int",
//        "Long",
//        "Float",
//        "Double",
//        "Boolean",
//        "ByteArray",
//        "CharArray",
//        "ShortArray",
//        "IntArray",
//        "LongArray",
//        "FloatArray",
//        "DoubleArray",
//        "BooleanArray",
//        "true",
//        "false",
//        "null",
//    )
}