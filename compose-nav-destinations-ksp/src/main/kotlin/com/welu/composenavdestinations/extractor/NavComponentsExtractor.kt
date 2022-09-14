package com.welu.composenavdestinations.extractor

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.welu.composenavdestinations.annotations.*
import com.welu.composenavdestinations.extensions.ksp.*
import com.welu.composenavdestinations.model.structure.*

typealias RawNavGraphWithRawNavDestinations = Pair<RawNavGraphInfo, Sequence<RawNavDestinationInfo>>

//TODO -> Eine sealed Class bauen, welche hier als Ergebnis zurückgegeben wird. Mit Fehlermeldungen etc und den benötigten Objekten. Dadurch muss man das nicht hier in der Datei definieren und
// kann die ganuen Meldungen zentral verwalten.
class NavComponentsExtractor(
    private val resolver: Resolver,
    private val logger: KSPLogger
) {

    //TODO -> Diese Structure muss jetzt noch in eine Form gemappt werden, dass man diese gescheit beim generator erstellen kann
    // Eigentlich sind nur die DIREKTEN NavDestinations und NavGraphs eines NavGraphs wichtig für den NavGraphSpec. Die Restlichen werden dann über die anderen Specs
    // hinzugefügt.
    fun extractAll(navDestinations: Sequence<KSClassDeclaration>): RawNavComponents {
        val navGraphAnnotations = resolver.getNavGraphDefinitions().let { navGraphs ->
            findDefaultNavGraphAnnotation()?.let { navGraphs + it } ?: navGraphs
        }

        checkUniqueNavGraphNames(navGraphAnnotations)
        checkContainsIsStartParameter(navGraphAnnotations)

        val extractionResult: Sequence<RawNavGraphWithRawNavDestinations> = extractRawNavGraphsWithRawNavDestinations(navGraphAnnotations)
        val rawNavGraphs = extractionResult.map(RawNavGraphWithRawNavDestinations::first)
        val rawNavGraphMap: MutableMap<KSClassDeclaration, RawNavGraphInfo> = rawNavGraphs.associateBy(RawNavGraphInfo::classDeclaration).toMutableMap()

        checkIfDestinationIsDefinedInOnlyOneNavGraph(rawNavGraphs)
        checkIfNavGraphIsDefinedInOnlyOneNavGraph(rawNavGraphs)
        checkIfEndNavGraphsContainDestination(rawNavGraphs)
        checkForCircularDependencies(rawNavGraphs, rawNavGraphMap)

        //TODO -> Das hier unten drunter vllt in einen einzelnen check umbauen
        val rootNavGraphs = findRootNavGraphs(rawNavGraphs)
        checkForNestedDefaultNavGraphs(rootNavGraphs, rawNavGraphMap)

        val defaultNavGraphs = rootNavGraphs.filter(RawNavGraphInfo::isDefaultNavGraph)
        checkDefaultNavGraphDefinition(defaultNavGraphs)

        val defaultNavGraph = defaultNavGraphs.first()

        val rootDestinations = extractRootDestinations(
            navDestinations = navDestinations,
            rawNavGraphs = rawNavGraphs,
            defaultNavGraph = defaultNavGraph
        )

        val finalRawNavDestinations = extractionResult.flatMap(RawNavGraphWithRawNavDestinations::second) + rootDestinations

        val rawNavGraphsWithRootDestinations = rawNavGraphs.map { navGraph ->
            if (navGraph.classDeclaration != defaultNavGraph.classDeclaration) return@map navGraph
            navGraph.copy(
                childNavDestinationSpecDeclarations = navGraph.childNavDestinationSpecDeclarations + rootDestinations.map(RawNavDestinationInfo::classDeclaration)
            ).also {
                rawNavGraphMap[navGraph.classDeclaration] = it
            }
        }

        val rawNavGraphComponentsMap = createRawNavGraphComponentsMap(
            rawNavGraphs = rawNavGraphsWithRootDestinations,
            rawNavDestinations = finalRawNavDestinations,
            rawNavGraphMap = rawNavGraphMap
        )

        checkIfStartingParameterIsSetForEachNavGraph(
            rawNavGraphs = rawNavGraphsWithRootDestinations,
            rawNavGraphComponentsMap = rawNavGraphComponentsMap
        )

        val finalRawNavGraphs = rawNavGraphsWithRootDestinations.map {
            it.copy(startComponentDeclaration = rawNavGraphComponentsMap[it.classDeclaration]!!.first(RawComponentInfo::isStart).classDeclaration)
        }

        return RawNavComponents(
            rawNavGraphInfos = finalRawNavGraphs,
            rawNavDestinationInfos = finalRawNavDestinations
        )
    }

    private fun createRawNavGraphComponentsMap(
        rawNavGraphs: Sequence<RawNavGraphInfo>,
        rawNavDestinations: Sequence<RawNavDestinationInfo>,
        rawNavGraphMap: MutableMap<KSClassDeclaration, RawNavGraphInfo>
    ): Map<KSClassDeclaration, Sequence<RawComponentInfo>> = rawNavGraphs.map { rawNavGraph ->
        val nestedNavGraphs = rawNavGraph.childNavGraphSpecDeclarations.mapNotNull(rawNavGraphMap::get)
        val destinations = rawNavDestinations.filter { rawNavGraph.childNavDestinationSpecDeclarations.contains(it.classDeclaration) }
        rawNavGraph.classDeclaration to (nestedNavGraphs + destinations)
    }.toMap()

    /**
     * Returns the [KSClassDeclaration] of the DefaultNavGraph, if present. This is needed to check if the DefaultNavGraph is actually used on any NavDestination
     * or NavGraph. If this returns null, then there has to be another NavGraph with "isDefaultNavGraph = true" present.
     */
    private fun findDefaultNavGraphAnnotation(): KSClassDeclaration? {
        //TODO -> Das besser machen, dass man einfach nur an die Annotation selber kommt und nicht die annotierten elemente anschauen muss
        //val defaultNavGraphAnnotated = resolver.getDefinitionsWithDefaultNavGraphAnnotation()

        return resolver.getDefinitionsWithDefaultNavGraphAnnotation()
            .firstOrNull()
            ?.getAnnotationWith(NavGraphDefaultDefinitionAnnotation)
            ?.annotationType
            ?.resolve()
            ?.declaration as KSClassDeclaration?
    }


    /**
     * Gibt [NavGraphStructure] instanzen zurück, welche die Verbindungen zwischen den einzelnen NavGraphs wiedergeben und die NavDestinations bündeln.
     */
    private fun extractRawNavGraphsWithRawNavDestinations(navGraphAnnotations: Sequence<KSClassDeclaration>): Sequence<RawNavGraphWithRawNavDestinations> =
        navGraphAnnotations.map { navGraphAnnotation ->
            val parentNavGraphs = navGraphAnnotations.filter { navGraphAnnotation.isAnnotationPresent(it) }

            if (parentNavGraphs.count() > 1) {
                throw IllegalStateException("The following NavGraph is a child of multiple NavGraphs: ${navGraphAnnotation.qualifiedName?.asString()}")
            }

            val parentNavGraph = parentNavGraphs.firstOrNull()
            val isStartOfParent = parentNavGraph?.let { navGraphAnnotation.getIsStartParameter(it) } ?: false

            val annotatedNodes = resolver.findAnnotatedNodesWith(navGraphAnnotation.qualifiedName!!.asString())
            val annotatedClassDeclarations = annotatedNodes.filterIsInstance<KSClassDeclaration>()

            val unknownNodes = annotatedNodes - annotatedClassDeclarations.toSet()

            if (unknownNodes.any()) {
                //TODO -> Mal noch schauen ob das so sinn ergibt mit dem output
                logger.error(
                    "Only classes can be annotated with this NavGraph Annotation: ${navGraphAnnotation.qualifiedName?.asString()}. " +
                            "Invalid Nodes: ${unknownNodes.joinToString(", ") { it.getFileReferenceText() }}",
                    navGraphAnnotation
                )
            }

            val annotatedGraphs = annotatedClassDeclarations.filter(navGraphAnnotations::contains)

            val annotatedDestinations = annotatedClassDeclarations.filter { it.isAnnotationPresentSimple(NavDestinationDefinitionAnnotation.import) }

            val invalidComponents = annotatedClassDeclarations - annotatedGraphs.toSet() - annotatedDestinations.toSet()

            if (invalidComponents.any()) {
                throw IllegalStateException(
                    "Unknown Components have been annotated with ${navGraphAnnotation.qualifiedName?.asString()}." +
                            "Only NavGraphs and NavDestinations can be annotated with such an Annotation. \n" +
                            "Unrecognized components: ${invalidComponents.joinToString(", ") { it.getFileReferenceText() }}"
                )
            }

            val rawDestinations = annotatedDestinations.map {
                RawNavDestinationInfo(
                    classDeclaration = it,
                    isStart = it.getIsStartParameter(navGraphAnnotation),
                    parentNavGraph = navGraphAnnotation
                )
            }


            val rawNavGraphInfo = RawNavGraphInfo(
                classDeclaration = navGraphAnnotation,
                route = navGraphAnnotation.getRouteParameter(),
                isDefaultNavGraph = navGraphAnnotation.getIsDefaultNavGraphParameter(),
                isStart = isStartOfParent,
                parentNavGraphSpecDeclaration = parentNavGraph,
                childNavGraphSpecDeclarations = annotatedGraphs,
                childNavDestinationSpecDeclarations = annotatedDestinations
            )

            RawNavGraphWithRawNavDestinations(rawNavGraphInfo, rawDestinations)
        }

    /**
     * Extracts all root Destinations which were not explicitly added to a NavGraph.
     *
     * They will be added to the DefaultNavGraphSpec or to a custom NavGraphSpec which has the "default" (default = true) parameter set tot true
     */
    private fun extractRootDestinations(
        navDestinations: Sequence<KSClassDeclaration>,
        rawNavGraphs: Sequence<RawNavGraphInfo>,
        defaultNavGraph: RawNavGraphInfo
    ): Sequence<RawNavDestinationInfo> = navDestinations.filter { destination ->
        rawNavGraphs.none { navGraph ->
            navGraph.childNavDestinationSpecDeclarations.contains(destination)
        }
    }.map {
        RawNavDestinationInfo(
            classDeclaration = it,
            parentNavGraph = defaultNavGraph.classDeclaration
        )
    }

    /**
     * Das sind die NavGraphs, die in keinem anderen NavGraph enthalten sind und somit die Navigation Root bilden
     */
    private fun findRootNavGraphs(rawNavGraphs: Sequence<RawNavGraphInfo>) = rawNavGraphs.filter { rawNavGraph ->
        rawNavGraphs.none { it.childNavGraphSpecDeclarations.contains(rawNavGraph.classDeclaration) }
    }


    /**
     * Checkt ob alle NavGraphDefinitions den isStart Parameter besitzen
     */
    @Throws(IllegalStateException::class)
    private fun checkContainsIsStartParameter(navGraphAnnotations: Sequence<KSClassDeclaration>) {
        val navGraphAnnotationsWithoutIsStartParam = navGraphAnnotations.filter {
            !it.isParameterPresent(NavGraphDefaultDefinitionAnnotation.IS_START_ARG)
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
     * Checks if there are any circular dependencies between NavGraphs.
     */
    @Throws(IllegalStateException::class)
    private fun checkForCircularDependencies(
        rawNavGraphInfos: Sequence<RawNavGraphInfo>,
        rawNavGraphMap: Map<KSClassDeclaration, RawNavGraphInfo>
    ) {

        fun checkNavGraphForCircularDependencies(
            navGraph: RawNavGraphInfo,
            visitedNodes: MutableList<KSClassDeclaration>
        ) {
            if (visitedNodes.contains(navGraph.classDeclaration)) {
                throw IllegalStateException("There is a circular dependency between the following NavGraphs: $visitedNodes")
            }

            visitedNodes.add(navGraph.classDeclaration)

            navGraph.childNavGraphSpecDeclarations.forEach { nestedGraph ->
                checkNavGraphForCircularDependencies(rawNavGraphMap[nestedGraph]!!, visitedNodes)
            }
        }

        rawNavGraphInfos.forEach { navGraph ->
            checkNavGraphForCircularDependencies(navGraph, mutableListOf())
        }
    }

    /**
     * Checks if a default NavGraph is defined in the Hierarchy
     */
    @Throws(IllegalStateException::class)
    private fun checkDefaultNavGraphDefinition(defaultNavGraphs: Sequence<RawNavGraphInfo>) {
        if (defaultNavGraphs.none()) {
            throw IllegalStateException(
                "There is no valid default NavGraphDefinition defined. Change 'isDefault = true' in one of your root NavGraphs " +
                        "OR annotate the starting Destination or NavGraph with @DefaultNavGraph(isStart = true)"
            )
        }

        if (defaultNavGraphs.count() > 1) {
            throw IllegalStateException(
                "There are multiple default NavGraphDefinitions present: ${defaultNavGraphs.joinToString(", ") { it.simpleName }}"
            )
        }
    }

    /**
     * Checkt ob es in der Hierarchie eines NavGraphs einen  NavGraph mit isDefaultNavGraph = true gibt
     */
    //TODO -> stattdessen NonRootNavGraphs übergeben und die hier dann verwendet. Dadurch braucht man keine rekursion mehr
    @Throws(IllegalStateException::class)
    private fun checkForNestedDefaultNavGraphs(
        rootNavGraphs: Sequence<RawNavGraphInfo>,
        rawNavGraphMap: Map<KSClassDeclaration, RawNavGraphInfo>
    ) {
        rootNavGraphs.forEach { rawNavGraph ->
            rawNavGraph.childNavGraphSpecDeclarations.forEach { nestedGraphDeclaration ->
                val nestedGraph = rawNavGraphMap[nestedGraphDeclaration]!!
                if (nestedGraph.isDefaultNavGraph) {
                    throw IllegalStateException(
                        "Nested NavGraphs cannot be the default NavGraph of an App. " +
                                "Set 'isDefaultNavGraph = false' on the following NavGraph Annotation: " + nestedGraph.simpleName
                    )
                } else {
                    checkForNestedDefaultNavGraphs(
                        rootNavGraphs = rawNavGraph.childNavGraphSpecDeclarations.mapNotNull(rawNavGraphMap::get),
                        rawNavGraphMap = rawNavGraphMap
                    )
                }
            }
        }
    }

    /**
     * Checkt ob es pro NavGraph Level GENAU eine Destination / NavGraph gibt, welcher isStart = true gesetzt hat
     */
    @Throws(IllegalStateException::class)
    private fun checkIfStartingParameterIsSetForEachNavGraph(
        rawNavGraphs: Sequence<RawNavGraphInfo>,
        rawNavGraphComponentsMap: Map<KSClassDeclaration, Sequence<RawComponentInfo>>
    ) {
        rawNavGraphs.forEach { navGraph ->
            val connectedRawComponents = rawNavGraphComponentsMap[navGraph.classDeclaration]!!
            val isStartRawComponents = connectedRawComponents.filter(RawComponentInfo::isStart)
            val isStartRawComponentsCount = isStartRawComponents.count()
            when {
                isStartRawComponentsCount == 0 -> throw IllegalStateException(
                    "There is no 'isStart' Component defined for the following NavGraph: ${navGraph.simpleName} - There has to be EXACTLY ONE NavGraph or Destination with 'isStart' set to true."
                )
                isStartRawComponentsCount > 1 -> throw IllegalStateException(
                    "Multiple 'isStart' Components are defined for the following NavGraph: ${navGraph.simpleName} - There has to be EXACTLY ONE NavGraph or Destination which has 'isStart' set to true.\n" +
                            "Components: ${isStartRawComponents.joinToString(", ") { it.classDeclaration.getFileReferenceText() }}"
                )
            }
        }
    }

    /**
     * Checkt ob eine Destination in genau einem oder keinem NavGraph vorhanden ist
     */
    @Throws(IllegalStateException::class)
    private fun checkIfDestinationIsDefinedInOnlyOneNavGraph(rawNavGraphs: Sequence<RawNavGraphInfo>) {
        rawNavGraphs
            .flatMap(RawNavGraphInfo::childNavDestinationSpecDeclarations)
            .groupingBy { it }
            .eachCount()
            .forEach {
                if (it.value != 1) {
                    throw IllegalStateException(
                        "The following Destination is a child of ${it.value} NavGraphs: ${it.key}.\n" +
                                "A Destination has to be the child of exactly ONE NavGraph."
                    )
                }
            }
    }

    /**
     * Checkt ob ein NavGraph in genau einem oder keinem NavGraph vorhanden ist
     */
    @Throws(IllegalStateException::class)
    private fun checkIfNavGraphIsDefinedInOnlyOneNavGraph(rawNavGraphs: Sequence<RawNavGraphInfo>) {
        rawNavGraphs
            .flatMap(RawNavGraphInfo::childNavGraphSpecDeclarations)
            .groupingBy { it }
            .eachCount()
            .forEach {
                if (it.value > 1) {
                    throw IllegalStateException(
                        "The following NavGraph is a child of ${it.value} different NavGraphs: ${it.key}.\n" +
                                "A NavGraph can only have ONE or ZERO parent NavGraphs."
                    )
                }
            }
    }

    /**
     * Checkt ob ein End NavGraph (nestedNavGraphs.size == 0) mindestens eine Destination besitzt.
     */
    @Throws(IllegalStateException::class)
    private fun checkIfEndNavGraphsContainDestination(rawNavGraphs: Sequence<RawNavGraphInfo>) {
        rawNavGraphs.forEach {
            if (it.childNavGraphSpecDeclarations.none() && it.childNavDestinationSpecDeclarations.none()) {
                throw IllegalStateException(
                    "The following NavGraph does not contain any Children: ${it.simpleName}"
                )
            }
        }
    }


    //TODO -> Das eventuell auch noch machen für Destinations und NavGraphs aber von dem
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


    //TODO -> Checken dass es einzigartige DestinationNames gibt -> Es darf keine zwei mit dem selben Namen geben, sonst Error.
    // Es darf auch keine NavGraphs geben mit dem gleichen Namen. Insgesamt müssen die Namen somit alle unique sein
//    @Throws(IllegalStateException::class)
//    private fun checkUniqueNavDestinationNames(navDestinations: Sequence<NavDestinationStructure>) {
//        if (navDestinations.distinctBy { it.simpleName }.count() == navDestinations.count()) return
//        val navGraphNameDeclarationMap = navDestinations.groupBy { it.simpleName }.filter { it.value.size != 1 }
//        throw IllegalStateException(
//            "Multiple NavDestinations have the same name: ${navGraphNameDeclarationMap.keys.joinToString(", ") { it }}"
//        )
//    }

    //TODO -> Checken dass es insgesamt einzigartige Routes gibt. Mit
    // Sollte man ganz am ende vllt machen und nicht hier. -> Dadurch kann man auch erst später die Route holen
//    @Throws(IllegalStateException::class)
//    private fun checkUniqueRoutes(
//        flatNavGraphDescriptions: Sequence<NavGraphStructure>
//    ) {
//        if (flatNavGraphDescriptions.distinctBy { it.route }.count() == flatNavGraphDescriptions.count()) return
//        val navGraphNameDeclarationMap = flatNavGraphDescriptions.groupBy { it.route }.filter { it.value.size != 1 }
//        throw IllegalStateException(
//            "Multiple NavGraphs have the same route: ${navGraphNameDeclarationMap.keys.joinToString(", ") { it }}"
//        )
//    }


    private fun KSClassDeclaration.getRouteParameter() = getAnnotationArgument<String>(
        argName = NavGraphDefinitionAnnotation.ROUTE_ARG,
        annotation = NavGraphDefinitionAnnotation
    ).ifBlank { simpleName.asString() }

    private fun KSClassDeclaration.getIsDefaultNavGraphParameter() = getAnnotationArgument<Boolean>(
        argName = NavGraphDefinitionAnnotation.IS_DEFAULT_NAV_GRAPH,
        annotation = NavGraphDefinitionAnnotation
    )

    private fun KSClassDeclaration.getIsStartParameter(annotation: KSClassDeclaration) = getAnnotationArgument<Boolean>(
        argName = "isStart",
        annotationName = annotation.simpleName.asString()
    )

    private fun KSClassDeclaration.getIsStartParameter() = getAnnotationArgument<Boolean>(
        argName = "isStart",
        annotationName = simpleName.asString()
    )
}