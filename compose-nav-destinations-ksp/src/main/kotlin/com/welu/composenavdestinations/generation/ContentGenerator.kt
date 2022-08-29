package com.welu.composenavdestinations.generation

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Resolver
import com.welu.composenavdestinations.model.NavDestinationInfo

class ContentGenerator(
    private val resolver: Resolver,
    private val codeGenerator: CodeGenerator
) {

    private val fileOutputWriter: FileOutputWriter by lazy {
        FileOutputWriter(codeGenerator, resolver)
    }

    fun generate(destinations: Sequence<NavDestinationInfo>) {
        CustomNavArgsFileContentGenerator(fileOutputWriter).generate(destinations)
        NavDestinationFileGenerator(fileOutputWriter).let { generator ->
            destinations.forEach(generator::generate)
        }
    }

}