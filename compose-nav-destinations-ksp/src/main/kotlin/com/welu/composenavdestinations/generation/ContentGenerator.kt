package com.welu.composenavdestinations.generation

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Resolver
import com.welu.composenavdestinations.model.NavDestinationInfo

class ContentGenerator(
    private val resolver: Resolver,
    private val codeGenerator: CodeGenerator
) {

    private val fileContentInfoOutputWriter: FileContentInfoOutputWriter by lazy {
        FileContentInfoOutputWriter(codeGenerator, resolver)
    }

    fun generate(destinations: Sequence<NavDestinationInfo>) {
        FileGeneratorCustomNavArgs.generate(destinations)?.let(fileContentInfoOutputWriter::writeFile)
        destinations.map(FileGeneratorNavDestination::generate).forEach(fileContentInfoOutputWriter::writeFile)
        FileGeneratorNavDestinationUtils.generate(destinations).let(fileContentInfoOutputWriter::writeFile)
    }

}