package com.welu.composenavdestinations.extensions.ksp

import com.google.devtools.ksp.containingFile
import com.google.devtools.ksp.symbol.*
import com.welu.composenavdestinations.extensions.file
import com.welu.composenavdestinations.extensions.reader
import java.io.BufferedReader

fun KSNode.getRootKSFile(): KSFile = containingFile
    ?: parent?.getRootKSFile()
    ?: throw IllegalStateException("Could not load the file for the following Node: $this")

fun KSNode.getFileLines() = (location as FileLocation).file.reader.use(BufferedReader::readLines)

fun KSNode.firstNFileLines(lineCount: Int): List<String> = getFileLines().take(lineCount)

val KSNode.lineNumber get(): Int = (location as FileLocation).lineNumber - 1

fun <R> KSNode.accept(visitor: KSVisitor<Unit, R>): R = accept(visitor, Unit)

//fun KSNode.firstNFileLines(lineCount: Int): List<String> = (location as FileLocation).file.reader.use {
//    it.lineSequence().take(lineCount).toList()
//
//}