package com.welu.composenavdestinations.generation

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Resolver
import com.welu.composenavdestinations.extensions.ksp.dependencies
import com.welu.composenavdestinations.extensions.write
import com.welu.composenavdestinations.model.FileContentInfo
import java.io.OutputStream

class FileContentInfoOutputWriter(
    private val codeGenerator: CodeGenerator,
    private val resolver: Resolver
) {

    fun writeFile(fileContentInfo: FileContentInfo) {
        codeGenerator.createNewFile(
            dependencies = resolver.dependencies,
            fileName = fileContentInfo.fileName,
            packageName = fileContentInfo.packageDir
        ).use { it.createFileWith(fileContentInfo) }
    }

    private fun OutputStream.createFileWith(fileContentInfo: FileContentInfo) {
        write("package " + fileContentInfo.packageDir, 2)
        fileContentInfo.imports.forEach {
            write(it.asImportLine, 1)
        }
        write("\n")
        write(fileContentInfo.code)
    }

}