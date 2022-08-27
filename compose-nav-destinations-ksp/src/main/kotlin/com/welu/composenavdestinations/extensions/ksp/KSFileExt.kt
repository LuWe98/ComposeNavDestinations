package com.welu.composenavdestinations.extensions.ksp

import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.welu.composenavdestinations.model.PackageImportInfo

val KSFile.firstDeclaration get(): KSDeclaration? = declarations.firstOrNull()

fun KSFile.extractImports(
    firstDeclarationRow: Int = firstDeclaration?.lineNumber ?: 0,
    lines: List<String> = firstNFileLines(firstDeclarationRow)
): List<PackageImportInfo> {

    var joined = ""
    for (index in 0..firstDeclarationRow) {
        joined += lines[index] + " "
    }

    val isInsideStringLookup = BooleanArray(joined.length)
    var isInsideString = false

    for (charIndex in 0..joined.lastIndex) {
        if (joined[charIndex] == '"' && (charIndex == 0 || joined[charIndex - 1] != '\\')) {
            isInsideString = !isInsideString
        }
        isInsideStringLookup[charIndex] = isInsideString
    }

    return PackageImportInfo.FILE_IMPORT_REGEX.findAll(joined).filter {
        !isInsideStringLookup[it.range.first]
    }.map{
        PackageImportInfo.fromImportLine(it.value)
    }.toList()
}