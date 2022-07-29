package com.welu.composenavdestinations.processors

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import com.welu.composenavdestinations.extensions.dependencies
import com.welu.composenavdestinations.extensions.getNavDestinations
import com.welu.composenavdestinations.output.NavDestinationFileGenerator
import com.welu.composenavdestinations.utils.Constants
import com.welu.composenavdestinations.mapper.NavDestinationMapper
import java.io.OutputStream

class NavDestinationsProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>
): SymbolProcessor {

    companion object {
        var _debugFile: OutputStream? = null
        val debugFile get(): OutputStream = _debugFile!!
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val annotatedNavDestinations = resolver.getNavDestinations()
        if(!annotatedNavDestinations.iterator().hasNext()) return emptyList()

        _debugFile = codeGenerator.createNewFile(
            dependencies = resolver.dependencies,
            packageName = Constants.PACKAGE_NAME,
            fileName = "LoggingFile"
        )

        val mapper = NavDestinationMapper(resolver, logger)
        val generator = NavDestinationFileGenerator(resolver, codeGenerator)

        val generatedClasses = annotatedNavDestinations.map(mapper::map)
        generatedClasses.forEach(generator::generate)

        _debugFile?.close()
        _debugFile = null

        return annotatedNavDestinations.filterNot(KSClassDeclaration::validate).toList()
    }
}