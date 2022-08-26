package com.welu.composenavdestinations.extensions.ksp

import com.google.devtools.ksp.containingFile
import com.google.devtools.ksp.symbol.FileLocation
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSVisitor
import com.welu.composenavdestinations.extensions.file
import com.welu.composenavdestinations.extensions.reader
import java.io.BufferedReader

fun <R> KSNode.accept(visitor: KSVisitor<Unit, R>): R = accept(visitor, Unit)

val KSNode.fileLines get() = (location as FileLocation).file.reader.use(BufferedReader::readLines)

fun KSNode.firstNFileLines(lineCount: Int): List<String> = (location as FileLocation).file.reader.use {
    it.lineSequence().take(lineCount).toList()
}

val KSNode.rootKSFile get(): KSFile = containingFile ?: parent?.rootKSFile ?: throw IllegalStateException()
