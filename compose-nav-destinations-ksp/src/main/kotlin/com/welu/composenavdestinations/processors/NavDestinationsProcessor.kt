package com.welu.composenavdestinations.processors

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.validate
import com.welu.composenavdestinations.extensions.ksp.dependencies
import com.welu.composenavdestinations.extensions.ksp.getNavArguments
import com.welu.composenavdestinations.extensions.ksp.getNavDestinations
import com.welu.composenavdestinations.extensions.ksp.isAnnotationPresent
import com.welu.composenavdestinations.generation.NavDestinationCodeGenerator
import com.welu.composenavdestinations.mapper.NavDestinationMapper
import com.welu.composenavdestinations.utils.PackageUtils
import com.welu.composenavdestinations.utils.PackageUtils.COMPOSABLE_IMPORT_INFO
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
        val annotatedNavDestinations = resolver.getNavDestinations()
        if (!annotatedNavDestinations.iterator().hasNext()) return emptyList()

        _debugFile = codeGenerator.createNewFile(
            dependencies = resolver.dependencies,
            packageName = PackageUtils.PACKAGE_NAME,
            fileName = "LoggingFile"
        )

        if (annotatedNavDestinations.any { !it.isAnnotationPresent(COMPOSABLE_IMPORT_INFO)  }) {
            throw IllegalStateException("NavDestination Annotation is only allowed on Composable-Functions")
        }

        val navArguments = resolver.getNavArguments()
        if (!navArguments.all { arg -> annotatedNavDestinations.any { arg.parent == it } }) {
            throw IllegalStateException("NavArgument Annotation is only allowed inside of NavDestination declaration.")
        }

        NavDestinationMapper(resolver, logger, navArguments).let { mapper ->
            NavDestinationCodeGenerator(resolver, codeGenerator, annotatedNavDestinations.map(mapper::map)).generate()
        }

        _debugFile?.close()
        _debugFile = null

        return annotatedNavDestinations.filterNot(KSFunctionDeclaration::validate).toList()
    }
}