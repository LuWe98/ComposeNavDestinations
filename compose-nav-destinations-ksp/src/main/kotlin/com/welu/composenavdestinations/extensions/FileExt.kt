package com.welu.composenavdestinations.extensions

import com.google.devtools.ksp.symbol.FileLocation
import java.io.*

fun OutputStream.write(str: String): OutputStream = apply {
    write(str.toByteArray())
}

fun OutputStream.writeComment(str: String): OutputStream = write("// $str")

val FileLocation.file get() = File(filePath)


val File.reader get() = BufferedReader(InputStreamReader(FileInputStream(this), Charsets.UTF_8))

fun <T> Reader.readUntilLine(lineNumber: Int, block: (Sequence<String>) -> T) = useLines { lines ->
    block(lines.take(lineNumber))
}

fun <T> Reader.readFromLine(lineNumber: Int, block: (Sequence<String>) -> T) = useLines { lines ->
    block(lines.drop(lineNumber))
}

fun <T> File.readUntilLine(lineNumber: Int, block: (Sequence<String>) -> T) {
    reader.readUntilLine(lineNumber, block)
}

fun <T> File.readFromLine(lineNumber: Int, block: (Sequence<String>) -> T) {
    reader.readFromLine(lineNumber, block)
}

fun File.readLine(lineNumber: Int): String = reader.use {
    it.readLines()[lineNumber]
}

fun File.readLines(from: Int, to: Int): String = reader.readLines().subList(from, to).reduce { acc, s -> acc + s + "\n" }

fun File.writeLinesTillLineNumber(lineNumber: Int) {
    readUntilLine(lineNumber) { lines ->
        var relevantLine: String

        lines.forEachIndexed { index, line ->
            if(index == lineNumber) {
                relevantLine = line
            }
        }
    }
}