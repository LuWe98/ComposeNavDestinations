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
import com.welu.composenavdestinations.extractor.DefaultValueExtractor
import com.welu.composenavdestinations.extractor.NavArgsInfoExtractor
import com.welu.composenavdestinations.extractor.RawNavComponentsExtractor
import com.welu.composenavdestinations.generation.ContentGenerator
import com.welu.composenavdestinations.mapper.NavDestinationsMapper
import com.welu.composenavdestinations.mapper.NavGraphsMapper

class NavDestinationsProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val annotatedDestinations = findAnnotatedNavDestinations(resolver).takeIf(Sequence<KSClassDeclaration>::any) ?: return emptyList()
        val annotatedNavGraphs = findAnnotatedNavGraphs(resolver)

        val rawNavComponents = RawNavComponentsExtractor(resolver, logger).extract(
            annotatedNavDestinations = annotatedDestinations,
            annotatedNavGraphs = annotatedNavGraphs
        )

        val defaultValueExtractor = DefaultValueExtractor(resolver)

        val navArgsInfoExtractor = NavArgsInfoExtractor(
            resolver = resolver,
            logger = logger,
            defaultValueExtractor = defaultValueExtractor
        )

        val navDestinationInfoMapper = NavDestinationsMapper(
            resolver = resolver,
            logger = logger,
            navArgsInfoExtractor = navArgsInfoExtractor
        )

        val navDestinationInfos = rawNavComponents.rawDestinationInfos.map(navDestinationInfoMapper::map)

        val navGraphInfoMapper = NavGraphsMapper(
            resolver = resolver,
            logger = logger,
            navArgsInfoExtractor = navArgsInfoExtractor
        )

        val navGraphInfos = rawNavComponents.rawNavGraphInfos.map(navGraphInfoMapper::map)

//        NavDestinationValidator.validate(
//            destinations = navDestinationInfos
//        )

        ContentGenerator(resolver, logger, codeGenerator).generate(
            navDestinations = navDestinationInfos,
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
        checkIfNavDestinationsAreOfTypeObject(annotatedNavDestinations)
        checkUniqueNavDestinationNames(annotatedNavDestinations)
        return annotatedNavDestinations
    }

    /**
     * Checkt ob es NavGraphDefinitions mit dem gleichen Namen gibt
     */
    @Throws(IllegalStateException::class)
    private fun checkUniqueNavGraphNames(navGraphAnnotations: Sequence<KSClassDeclaration>) {
        if (navGraphAnnotations.distinctBy { it.simpleName.asString() }.count() == navGraphAnnotations.count()) return
        val navGraphNameDeclarationMap = navGraphAnnotations.groupBy { it.simpleName.asString() }.filter { it.value.size != 1 }
        throw IllegalStateException(
            "Multiple NavGraphs have the same name: ${navGraphNameDeclarationMap.keys.joinToString(", ") { it }}"
        )
    }

    @Throws(IllegalStateException::class)
    private fun checkIfNavDestinationsAreOfTypeObject(navDestinations: Sequence<KSClassDeclaration>) {
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

    //TODO -> Checken dass es einzigartige DestinationNames gibt -> Es darf keine zwei mit dem selben Namen geben, sonst Error.
    // Es darf auch keine NavGraphs geben mit dem gleichen Namen. Insgesamt müssen die Namen somit alle unique sein
    @Throws(IllegalStateException::class)
    private fun checkUniqueNavDestinationNames(navDestinations: Sequence<KSClassDeclaration>) {
        if (navDestinations.distinctBy { it.simpleName }.count() == navDestinations.count()) return
        val navGraphNameDeclarationMap = navDestinations.groupBy { it.simpleName.asString() }.filter { it.value.size != 1 }
        throw IllegalStateException(
            "Multiple NavDestinations have the same name: ${navGraphNameDeclarationMap.keys.joinToString(", ") { it }}"
        )
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
}