package com.welu.compose_nav_destinations_ksp.generation

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Resolver
import com.welu.compose_nav_destinations_ksp.extensions.ksp.dependencies
import com.welu.compose_nav_destinations_ksp.extensions.write
import com.welu.compose_nav_destinations_ksp.model.FileContentInfo
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