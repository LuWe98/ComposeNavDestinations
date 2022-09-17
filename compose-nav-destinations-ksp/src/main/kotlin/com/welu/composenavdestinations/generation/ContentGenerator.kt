package com.welu.composenavdestinations.generation

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Resolver
import com.welu.composenavdestinations.generation.destinations.FileGeneratorDestinationExt
import com.welu.composenavdestinations.generation.destinations.FileGeneratorDestinationSpec
import com.welu.composenavdestinations.generation.general.FileGeneratorCustomNavArgs
import com.welu.composenavdestinations.generation.general.FileGeneratorNavComponentUtils
import com.welu.composenavdestinations.generation.general.FileGeneratorResultExtensions
import com.welu.composenavdestinations.generation.graphs.FileGeneratorNavGraphSpec
import com.welu.composenavdestinations.model.components.NavDestinationInfo
import com.welu.composenavdestinations.model.components.NavGraphInfo

class ContentGenerator(
    private val resolver: Resolver,
    private val codeGenerator: CodeGenerator
) {

    private val fileContentInfoOutputWriter: FileContentInfoOutputWriter by lazy {
        FileContentInfoOutputWriter(codeGenerator, resolver)
    }

    fun generate(
        navDestinations: Sequence<NavDestinationInfo>,
        navGraphs: Sequence<NavGraphInfo>
    ) {

        //Generates the custom NavArgs needed for Navigation
        FileGeneratorCustomNavArgs.generate(navDestinations)?.let(fileContentInfoOutputWriter::writeFile)

        //Generates the NavDestinationUtils File
        FileGeneratorNavComponentUtils.generate(navDestinations).let(fileContentInfoOutputWriter::writeFile)

        //Generates the NavDestinationsExt File
        FileGeneratorDestinationExt.generate(navDestinations).let(fileContentInfoOutputWriter::writeFile)

        //Generates the NavDestinationsResultExt File
        FileGeneratorResultExtensions.generate(navDestinations).let(fileContentInfoOutputWriter::writeFile)

        //Generates the DestinationSpecs for all annotated destinations
        navDestinations.map(FileGeneratorDestinationSpec::generate).forEach(fileContentInfoOutputWriter::writeFile)

        //Generates the NavGraphSpecs for all annotated NavGraphs
        navGraphs.mapNotNull(FileGeneratorNavGraphSpec::generate).forEach(fileContentInfoOutputWriter::writeFile)

    }
}