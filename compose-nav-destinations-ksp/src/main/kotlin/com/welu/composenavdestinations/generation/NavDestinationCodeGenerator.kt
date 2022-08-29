package com.welu.composenavdestinations.generation

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Resolver
import com.welu.composenavdestinations.model.NavDestinationInfo

class NavDestinationCodeGenerator(
    private val resolver: Resolver,
    private val codeGenerator: CodeGenerator,
    private val destinations: Sequence<NavDestinationInfo>
) {

    fun generate() {
        NavDestinationCustomNavArgsFileGenerator(resolver, codeGenerator).generate(destinations)

        NavDestinationFileGenerator(resolver, codeGenerator).let { generator ->
            destinations.forEach(generator::generate)
        }
    }

}