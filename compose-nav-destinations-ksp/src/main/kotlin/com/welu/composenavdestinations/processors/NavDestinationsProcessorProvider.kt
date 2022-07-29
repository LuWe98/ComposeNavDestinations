package com.welu.composenavdestinations.processors

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class NavDestinationsProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment) = NavDestinationsProcessor(
        environment.codeGenerator,
        environment.logger,
        environment.options
    )
}