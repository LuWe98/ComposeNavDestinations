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

        //Generates the custom NavArgs needed for Navigation
        FileGeneratorCustomNavArgs.generate(destinations)?.let(fileContentInfoOutputWriter::writeFile)

        //Generates the NavDestinationUtils File
        FileGeneratorNavDestinationUtils.generate(destinations).let(fileContentInfoOutputWriter::writeFile)

        //Generates the NavDestinationsExt File
        FileGeneratorDestinationExt.generate(destinations).let(fileContentInfoOutputWriter::writeFile)

        //Generates the NavDestinationsResultExt File
        FileGeneratorResultExtensions.generate(destinations).let(fileContentInfoOutputWriter::writeFile)

        //Generates the DestinationSpecs for all annotated destinations
        destinations.map(FileGeneratorDestinationSpec::generate).forEach(fileContentInfoOutputWriter::writeFile)
    }

}