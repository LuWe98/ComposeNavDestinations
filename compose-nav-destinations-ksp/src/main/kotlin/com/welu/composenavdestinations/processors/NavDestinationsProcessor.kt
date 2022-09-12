package com.welu.composenavdestinations.processors

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import com.welu.composenavdestinations.extensions.ksp.dependencies
import com.welu.composenavdestinations.extensions.ksp.getNavDestinationDefinitions
import com.welu.composenavdestinations.generation.ContentGenerator
import com.welu.composenavdestinations.mapper.MapperNavDestinations
import com.welu.composenavdestinations.mapper.MapperNavigationStructure
import com.welu.composenavdestinations.utils.PackageUtils
import com.welu.composenavdestinations.validation.NavDestinationValidator
import java.io.OutputStream

class NavDestinationsProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>
) : SymbolProcessor {

    companion object {
        private var _debugFile: OutputStream? = null
        val debugFile get(): OutputStream = _debugFile!!
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {

        val definitions = resolver.getNavDestinationDefinitions()
        if (definitions.none()) return emptyList()
        if (definitions.any { it.classKind != ClassKind.OBJECT }) {
            throw IllegalStateException("Only Destinations of type Object are supported!")
        }

        val mapper = MapperNavDestinations(resolver, logger)
        val navDestinationInfos = definitions.map(mapper::map)

        NavDestinationValidator.validate(
            destinations = navDestinationInfos
        )

        ContentGenerator(resolver, codeGenerator).generate(
            destinations = navDestinationInfos
        )

        _debugFile = codeGenerator.createNewFile(
            dependencies = resolver.dependencies,
            packageName = PackageUtils.PACKAGE_NAME,
            fileName = "LoggingFile"
        )

        //Das holt die Struktur raus aus von dem Navigation -> Bezieht sich auf NavGraphSpec, NavDestinationSpec
        MapperNavigationStructure().annotationTests(resolver, definitions)

        _debugFile?.close()
        _debugFile = null

        return definitions.filterNot(KSClassDeclaration::validate).toList()
    }

}