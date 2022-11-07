package com.welu.composenavdestinations.processors

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.welu.composenavdestinations.annotations.DefaultComposeNavGraphAnnotation
import com.welu.composenavdestinations.extensions.ksp.*
import com.welu.composenavdestinations.extractor.NavArgsInfoExtractor
import com.welu.composenavdestinations.extractor.RawNavComponentsExtractor
import com.welu.composenavdestinations.generation.ContentGenerator
import com.welu.composenavdestinations.mapper.ComposeDestinationMapper
import com.welu.composenavdestinations.mapper.ComposeNavGraphMapper
import kotlinx.coroutines.*

class NavDestinationsProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val annotatedDestinations = findAnnotatedNavDestinations(resolver).takeIf(Sequence<*>::any) ?: return emptyList()
        val annotatedNavGraphs = findAnnotatedNavGraphs(resolver)

        val rawNavComponents = RawNavComponentsExtractor(resolver, logger).extract(
            annotatedNavDestinations = annotatedDestinations,
            annotatedNavGraphs = annotatedNavGraphs
        )

        val navArgsInfoExtractor = NavArgsInfoExtractor(resolver, logger)

        val navDestinationInfoMapper = ComposeDestinationMapper(resolver, logger, navArgsInfoExtractor)

        val navDestinationInfos = rawNavComponents.rawDestinationInfos.map(navDestinationInfoMapper::map)

        val navGraphInfoMapper = ComposeNavGraphMapper(resolver, logger, navArgsInfoExtractor)

        val navGraphInfos = rawNavComponents.rawNavGraphInfos.map(navGraphInfoMapper::map)

        ContentGenerator(resolver, logger, codeGenerator).generate(
            destinations = navDestinationInfos,
            navGraphs = navGraphInfos
        )

        return emptyList()
    }

    /**
     * Returns the [KSClassDeclaration] of the DefaultNavGraph, if present. This is needed to check if the DefaultNavGraph is actually used on any NavDestination
     * or NavGraph. If this returns null, then there has to be another NavGraph with "isDefaultNavGraph = true" present.
     */
    private fun findDefaultNavGraphAnnotation(resolver: Resolver): KSClassDeclaration? {
        //TODO -> Das besser machen, dass man einfach nur an die Annotation selber kommt und nicht die annotierten elemente anschauen muss
        //val defaultNavGraphAnnotated = resolver.getDefinitionsWithDefaultNavGraphAnnotation()
        return resolver.getDefinitionsWithDefaultNavGraphAnnotation()
            .firstOrNull()
            ?.getAnnotationWith(DefaultComposeNavGraphAnnotation)
            ?.annotationType
            ?.resolve()
            ?.declaration as KSClassDeclaration?
    }

    private fun findAnnotatedNavGraphs(resolver: Resolver): Sequence<KSClassDeclaration> {
        val annotatedNavGraphs = resolver.findNavGraphDefinitions().let { navGraphs ->
            findDefaultNavGraphAnnotation(resolver)?.let { navGraphs + it } ?: navGraphs
        }
        checkUniqueNavGraphNames(annotatedNavGraphs)
        checkContainsIsStartParameter(annotatedNavGraphs)
        return annotatedNavGraphs
    }

    private fun findAnnotatedNavDestinations(resolver: Resolver): Sequence<KSClassDeclaration> {
        val annotatedNavDestinations = resolver.findNavDestinationDefinitions()
        checkIfDestinationsClassKindIsObject(annotatedNavDestinations)
        checkUniqueNavDestinationNames(annotatedNavDestinations)
        return annotatedNavDestinations
    }


    @Throws(IllegalStateException::class)
    private fun checkIfDestinationsClassKindIsObject(navDestinations: Sequence<KSClassDeclaration>) {
        //it.isCompanionObject since a CompanionObject does not have a name -> Will be maybe supported in the future
        val nonObjectNavDestinations = navDestinations.filter { it.classKind != ClassKind.OBJECT || it.isCompanionObject }
        if (nonObjectNavDestinations.any()) {
            throw IllegalStateException(
                "Only Destinations of type Object are supported! Non Object NavDestinations: " + nonObjectNavDestinations.joinToString(", ") {
                    it.getFileReferenceText()
                }
            )
        }
    }

    /**
     * Checkt ob alle NavGraphDefinitions den isStart Parameter besitzen
     */
    @Throws(IllegalStateException::class)
    private fun checkContainsIsStartParameter(navGraphAnnotations: Sequence<KSClassDeclaration>) {
        val navGraphAnnotationsWithoutIsStartParam = navGraphAnnotations.filter {
            !it.isParameterPresent(DefaultComposeNavGraphAnnotation.IS_START_ARG)
        }

        if (navGraphAnnotationsWithoutIsStartParam.none()) return

        throw IllegalStateException(
            "The following NavGraphDefinitions do not have the required isStart Parameter: " +
                    navGraphAnnotationsWithoutIsStartParam.joinToString(", ") {
                        it.qualifiedName?.asString() ?: it.simpleName.asString()
                    }
        )
    }

    /**
     * Checks if there are duplicate NavGraph names
     */
    @Throws(IllegalStateException::class)
    private fun checkUniqueNavGraphNames(navGraphAnnotations: Sequence<KSClassDeclaration>) {
        if (navGraphAnnotations.distinctBy { it.simpleName.asString() }.count() == navGraphAnnotations.count()) return
        val navGraphNameDeclarationMap = navGraphAnnotations.groupBy { it.simpleName.asString() }.filter { it.value.size != 1 }
        throw IllegalStateException(
            "Multiple NavGraphs have the same name: ${navGraphNameDeclarationMap.keys.joinToString(", ") { it }}"
        )
    }

    /**
     * Checks if there are duplicate Destination names
     */
    @Throws(IllegalStateException::class)
    private fun checkUniqueNavDestinationNames(navDestinations: Sequence<KSClassDeclaration>) {
        if (navDestinations.distinctBy { it.simpleName.asString() }.count() == navDestinations.count()) return
        val navGraphNameDeclarationMap = navDestinations.groupBy { it.simpleName.asString() }.filter { it.value.size != 1 }
        throw IllegalStateException(
            "Multiple NavDestinations have the same name: ${navGraphNameDeclarationMap.keys.joinToString(", ") { it }}"
        )
    }

    //    @Throws(IllegalStateException::class)
//    private fun checkUniqueNavComponentNames(navComponents: Sequence<KSClassDeclaration>) {
//        if (navComponents.distinctBy { it.simpleName.asString() }.count() == navComponents.count()) return
//        val nameDeclarationMap = navComponents.groupBy { it.simpleName.asString() }.filter { it.value.size != 1 }
//        throw IllegalStateException(
//            "Multiple Destinations or NavGraphs have the same name: ${nameDeclarationMap.keys.joinToString(", ") { it }}"
//        )
//    }

}