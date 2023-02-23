package com.welu.compose_nav_destinations_ksp.extensions.ksp

import com.google.devtools.ksp.containingFile
import com.google.devtools.ksp.symbol.*
import com.welu.compose_nav_destinations_ksp.extensions.file
import com.welu.compose_nav_destinations_ksp.extensions.reader
import java.io.BufferedReader

fun KSNode.findFileLocation() = if(location is FileLocation) location as FileLocation else null

val KSNode.codeLineNumber get(): Int = findFileLocation()?.let { it.lineNumber - 1  } ?: 0

/**
 * This is used to get a String/Text Reference of the node. This will act like a link a user can click to jump to the right code position.
 */
fun KSNode.getFileReferenceText(): String  = "${findFileLocation()?.file?.path ?: getRootKSFile().filePath}:$codeLineNumber:"

fun KSNode.getRootKSFile(): KSFile = containingFile
    ?: parent?.getRootKSFile()
    ?: throw IllegalStateException("Could not load the file for the following Node: $this")

fun KSNode.getFileLines(): List<String> = findFileLocation()?.file?.reader?.use(BufferedReader::readLines) ?: emptyList()

fun KSNode.firstNFileLines(lineCount: Int): List<String> {
    val reader = findFileLocation()?.file?.reader ?: return emptyList()

    return reader.useLines {
        it.take(lineCount).toList()
    }
}

//fun KSNode.firstNFileLines(lineCount: Int) = findFileLocation()?.file?.reader?.use {
//    //it.lines().limit(lineCount.toLong()).toList()
//    //val a = it.lines().limit(lineCount.toLong())
//
//    it.readLines().take(lineCount)
//} ?: emptyList()

//fun KSNode.firstNFileLines(lineCount: Int): List<String> = (location as FileLocation).file.reader.use {
//    it.lineSequence().take(lineCount).toList()
//
//}

fun <R> KSNode.accept(visitor: KSVisitor<Unit, R>): R = accept(visitor, Unit)
