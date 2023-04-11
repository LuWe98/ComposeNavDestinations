package com.welu.compose_nav_destinations_ksp.processors

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.welu.compose_nav_destinations_ksp.extensions.ksp.dependencies
import com.welu.compose_nav_destinations_ksp.extractor.NavArgsInfoExtractor
import com.welu.compose_nav_destinations_ksp.extractor.RawNavComponentsExtractor
import com.welu.compose_nav_destinations_ksp.generation.FileGenerator
import com.welu.compose_nav_destinations_ksp.mapper.ComposeDestinationMapper
import com.welu.compose_nav_destinations_ksp.mapper.ComposeNavGraphMapper
import com.welu.compose_nav_destinations_ksp.resolver.NavComponentResolver

class NavDestinationsProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {

        val navComponentResolver = NavComponentResolver(resolver)

        val annotatedDestinations = navComponentResolver.findAnnotatedNavDestinations().takeIf(List<*>::isNotEmpty) ?: return emptyList()

        val annotatedNavGraphs = navComponentResolver.findAnnotatedNavGraphs()

        val rawNavComponents = RawNavComponentsExtractor(resolver, logger).extract(
            annotatedNavDestinations = annotatedDestinations,
            annotatedNavGraphs = annotatedNavGraphs
        )

        val navArgsInfoExtractor = NavArgsInfoExtractor(resolver, logger)

        val navDestinationInfoMapper = ComposeDestinationMapper(logger, navArgsInfoExtractor)
        val navDestinationInfos = rawNavComponents.rawDestinationInfos.map(navDestinationInfoMapper::map)

        val navGraphInfoMapper = ComposeNavGraphMapper(logger, navArgsInfoExtractor)
        val navGraphInfos = rawNavComponents.rawNavGraphInfos.map(navGraphInfoMapper::map)

        FileGenerator(resolver.dependencies, codeGenerator, logger).generate(
            destinations = navDestinationInfos,
            navGraphs = navGraphInfos
        )

        return emptyList()
    }

}