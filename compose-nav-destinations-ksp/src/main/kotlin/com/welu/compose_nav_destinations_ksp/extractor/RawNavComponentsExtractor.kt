package com.welu.compose_nav_destinations_ksp.extractor

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.welu.compose_nav_destinations_ksp.annotations.ComposeDestinationAnnotation
import com.welu.compose_nav_destinations_ksp.annotations.ComposeNavGraphAnnotation
import com.welu.compose_nav_destinations_ksp.extensions.ksp.findAnnotatedNodesWith
import com.welu.compose_nav_destinations_ksp.extensions.ksp.getAnnotationArgument
import com.welu.compose_nav_destinations_ksp.extensions.ksp.getFileReferenceText
import com.welu.compose_nav_destinations_ksp.extensions.ksp.hasAnnotation
import com.welu.compose_nav_destinations_ksp.extensions.ksp.hasAnnotationSimple
import com.welu.compose_nav_destinations_ksp.model.components.rawcomponents.RawComposeDestinationInfo
import com.welu.compose_nav_destinations_ksp.model.components.rawcomponents.RawComposeNavGraphInfo
import com.welu.compose_nav_destinations_ksp.model.components.rawcomponents.RawNavComponentInfo
import com.welu.compose_nav_destinations_ksp.model.components.rawcomponents.RawNavComponents

typealias RawNavGraphWithRawNavDestinations = Pair<RawComposeNavGraphInfo, List<RawComposeDestinationInfo>>

//TODO -> NextStep:
// 1. Implementierung von einem Validator, welcher das checking, welches noch hier drin steht übernimmt. Dadurch ist der Code besser aufgeräumt
// 2. Route abfragen bei den NavDestinations, dass man dadurch easy hier schon validieren kann ob es insgesamt einzigartige Routen gibt oder nicht.
//TODO -> Eine sealed Class bauen, welche hier als Ergebnis zurückgegeben wird. Mit Fehlermeldungen etc und den benötigten Objekten. Dadurch muss man das nicht hier in der Datei definieren und
// kann die ganuen Meldungen zentral verwalten.

class RawNavComponentsExtractor(
    private val resolver: Resolver,
    private val logger: KSPLogger
) {

    fun extract(
        annotatedNavDestinations: List<KSClassDeclaration>,
        annotatedNavGraphs: List<KSClassDeclaration>
    ): RawNavComponents {
        val navGraphWithDestinationsSequence = extractRawNavGraphsWithRawNavDestinations(annotatedNavGraphs)
        val rawNavGraphs = navGraphWithDestinationsSequence.map(RawNavGraphWithRawNavDestinations::first)
        val rawNavGraphMap: MutableMap<KSClassDeclaration, RawComposeNavGraphInfo> = rawNavGraphs.associateBy(RawComposeNavGraphInfo::classDeclaration).toMutableMap()

        validateIfDestinationIsDefinedInExactlyOneNavGraph(rawNavGraphs = rawNavGraphs)
        validateIfNavGraphIsDefinedInAtMostOneNavGraph(rawNavGraphs = rawNavGraphs)
        validateIfLeafNavGraphsContainDestination(rawNavGraphs = rawNavGraphs)
        validateIfNoCircularDependenciesArePresent(rawNavGraphs = rawNavGraphs, rawNavGraphMap = rawNavGraphMap)

        //TODO -> Das hier unten drunter vllt in einen einzelnen check umbauen, mit den default NavGraphs
        val rootNavGraphs = findRootNavGraphs(rawNavGraphs)
        validateForNestedDefaultNavGraphs(rootNavGraphs = rootNavGraphs, rawNavGraphMap = rawNavGraphMap)

        val defaultNavGraphs = rootNavGraphs.filter(RawComposeNavGraphInfo::isDefaultNavGraph)
        validateDefaultNavGraphDefinition(defaultNavGraphs = defaultNavGraphs)

        val defaultNavGraph = defaultNavGraphs.first()

        val rootDestinations = extractRawRootDestinations(
            navDestinations = annotatedNavDestinations,
            rawNavGraphs = rawNavGraphs,
            defaultNavGraph = defaultNavGraph
        )

        val finalRawNavDestinations = navGraphWithDestinationsSequence.flatMap(RawNavGraphWithRawNavDestinations::second) + rootDestinations

        validateIfAllRoutesAreUnique(rawNavGraphs = rawNavGraphs, rawNavDestinations = finalRawNavDestinations)

        val rawNavGraphsWithRootDestinations = rawNavGraphs.map { navGraph ->
            if (navGraph.classDeclaration != defaultNavGraph.classDeclaration) return@map navGraph
            navGraph.copy(
                childNavDestinationSpecDeclarations = navGraph.childNavDestinationSpecDeclarations + rootDestinations.map(RawComposeDestinationInfo::classDeclaration)
            ).also {
                rawNavGraphMap[navGraph.classDeclaration] = it
            }
        }

        val rawNavGraphComponentsMap = createRawNavGraphComponentsMap(
            rawNavGraphs = rawNavGraphsWithRootDestinations,
            rawNavDestinations = finalRawNavDestinations,
            rawNavGraphMap = rawNavGraphMap
        )

        validateIfStartingParameterIsSetForEachNavGraph(
            rawNavGraphs = rawNavGraphsWithRootDestinations,
            rawNavGraphComponentsMap = rawNavGraphComponentsMap
        )

        val finalRawNavGraphs = rawNavGraphsWithRootDestinations.map {
            it.copy(startComponentDeclaration = rawNavGraphComponentsMap[it.classDeclaration]!!.first(RawNavComponentInfo::isStart))
        }

        return RawNavComponents(
            rawNavGraphInfos = finalRawNavGraphs,
            rawDestinationInfos = finalRawNavDestinations
        )
    }

    private fun createRawNavGraphComponentsMap(
        rawNavGraphs: List<RawComposeNavGraphInfo>,
        rawNavDestinations: List<RawComposeDestinationInfo>,
        rawNavGraphMap: MutableMap<KSClassDeclaration, RawComposeNavGraphInfo>
    ): Map<KSClassDeclaration, List<RawNavComponentInfo>> = rawNavGraphs.map { rawNavGraph ->
        val nestedNavGraphs = rawNavGraph.childNavGraphSpecDeclarations.mapNotNull(rawNavGraphMap::get)
        val destinations = rawNavDestinations.filter { rawNavGraph.childNavDestinationSpecDeclarations.contains(it.classDeclaration) }
        rawNavGraph.classDeclaration to (nestedNavGraphs + destinations)
    }.toMap()


    /**
     * Gibt [NavGraphStructure] instanzen zurück, welche die Verbindungen zwischen den einzelnen NavGraphs wiedergeben und die NavDestinations bündeln.
     */
    private fun extractRawNavGraphsWithRawNavDestinations(annotatedNavGraphs: List<KSClassDeclaration>) = annotatedNavGraphs.map { annotatedNavGraph ->

        val annotatedNodes = resolver.findAnnotatedNodesWith(annotatedNavGraph.qualifiedName!!.asString())

        val annotatedClassDeclarations = annotatedNodes.filterIsInstance<KSClassDeclaration>().toList()

        val unknownNodes = annotatedNodes - annotatedClassDeclarations.toSet()

        if (unknownNodes.any()) {
            //TODO -> Mal noch schauen ob das so sinn ergibt mit dem output
            logger.error(
                "Only classes can be annotated with this NavGraph Annotation: ${annotatedNavGraph.qualifiedName?.asString()}. " +
                        "Invalid Nodes: ${unknownNodes.joinToString(", ") { it.getFileReferenceText() }}",
                annotatedNavGraph
            )
        }

        val annotatedGraphs = annotatedClassDeclarations.filter(annotatedNavGraphs::contains)

        val annotatedDestinations = annotatedClassDeclarations.filter { it.hasAnnotationSimple(ComposeDestinationAnnotation.import) }

        val invalidComponents = annotatedClassDeclarations - annotatedGraphs.toSet() - annotatedDestinations.toSet()

        if (invalidComponents.any()) {
            throw IllegalStateException(
                "Unknown Components have been annotated with ${annotatedNavGraph.qualifiedName?.asString()}." +
                        "Only NavGraphs and NavDestinations can be annotated with such an Annotation. \n" +
                        "Unrecognized components: ${invalidComponents.joinToString(", ") { it.getFileReferenceText() }}"
            )
        }

        val parentNavGraphs = annotatedNavGraphs.filter(annotatedNavGraph::hasAnnotation)

        if (parentNavGraphs.count() > 1) {
            throw IllegalStateException("The following NavGraph is a child of multiple NavGraphs: ${annotatedNavGraph.qualifiedName?.asString()}")
        }

        val parentNavGraph = parentNavGraphs.firstOrNull()

        val isStartOfParent = parentNavGraph?.let { annotatedNavGraph.getIsStartParameter(it) } ?: false

        val rawNavGraphInfo = RawComposeNavGraphInfo(
            classDeclaration = annotatedNavGraph,
            baseRoute = annotatedNavGraph.getNavGraphRouteParameter(),
            isDefaultNavGraph = annotatedNavGraph.getIsDefaultNavGraphParameter(),
            isStart = isStartOfParent,
            parentNavGraphSpecDeclaration = parentNavGraph,
            childNavGraphSpecDeclarations = annotatedGraphs,
            childNavDestinationSpecDeclarations = annotatedDestinations
        )

        val rawDestinations = annotatedDestinations.map {
            RawComposeDestinationInfo(
                classDeclaration = it,
                baseRoute = it.getNavDestinationRouteParameter(),
                isStart = it.getIsStartParameter(annotatedNavGraph),
                parentNavGraph = annotatedNavGraph
            )
        }

        RawNavGraphWithRawNavDestinations(
            rawNavGraphInfo,
            rawDestinations
        )
    }

    /**
     * Extracts all root Destinations which were not explicitly added to a NavGraph.
     *
     * They will be added to the DefaultNavGraphSpec or to a custom NavGraphSpec which has the "default" (default = true) parameter set tot true
     */
    private fun extractRawRootDestinations(
        navDestinations: List<KSClassDeclaration>,
        rawNavGraphs: List<RawComposeNavGraphInfo>,
        defaultNavGraph: RawComposeNavGraphInfo
    ): List<RawComposeDestinationInfo> = navDestinations.filter { destination ->
        rawNavGraphs.none { navGraph ->
            navGraph.childNavDestinationSpecDeclarations.contains(destination)
        }
    }.map {
        RawComposeDestinationInfo(
            classDeclaration = it,
            parentNavGraph = defaultNavGraph.classDeclaration,
            baseRoute = it.getNavDestinationRouteParameter()
        )
    }

    /**
     * Das sind die NavGraphs, die in keinem anderen NavGraph enthalten sind und somit eine Navigation Root bilden
     */
    private fun findRootNavGraphs(rawNavGraphs: List<RawComposeNavGraphInfo>) = rawNavGraphs.filter { rawNavGraph ->
        rawNavGraphs.none { it.childNavGraphSpecDeclarations.contains(rawNavGraph.classDeclaration) }
    }

    /**
     * Checks if there are any circular dependencies between NavGraphs.
     */
    @Throws(IllegalStateException::class)
    private fun validateIfNoCircularDependenciesArePresent(
        rawNavGraphs: List<RawComposeNavGraphInfo>,
        rawNavGraphMap: Map<KSClassDeclaration, RawComposeNavGraphInfo>
    ) {

        fun checkNavGraphForCircularDependencies(
            navGraph: RawComposeNavGraphInfo,
            visitedNodes: MutableSet<KSClassDeclaration>
        ) {
            if (!visitedNodes.add(navGraph.classDeclaration)) {
                throw IllegalStateException("There is a circular dependency between the following NavGraphs: $visitedNodes")
            }

            navGraph.childNavGraphSpecDeclarations.forEach { nestedGraph ->
                checkNavGraphForCircularDependencies(rawNavGraphMap[nestedGraph]!!, visitedNodes)
            }
        }

        rawNavGraphs.forEach { navGraph ->
            checkNavGraphForCircularDependencies(navGraph, mutableSetOf())
        }
    }

    /**
     * Checks if a default NavGraph is defined in the Hierarchy
     */
    @Throws(IllegalStateException::class)
    private fun validateDefaultNavGraphDefinition(defaultNavGraphs: List<RawComposeNavGraphInfo>) {
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
    private fun validateForNestedDefaultNavGraphs(
        rootNavGraphs: List<RawComposeNavGraphInfo>,
        rawNavGraphMap: Map<KSClassDeclaration, RawComposeNavGraphInfo>
    ) {
        rootNavGraphs.forEach { rawNavGraph ->
            rawNavGraph.childNavGraphSpecDeclarations.forEach { nestedGraphDeclaration ->
                if (rawNavGraphMap[nestedGraphDeclaration]!!.isDefaultNavGraph) {
                    throw IllegalStateException(
                        "Nested NavGraphs cannot be the default NavGraph of an App. " +
                                "Set 'isDefaultNavGraph = false' on the following NavGraph Annotation: " + rawNavGraphMap[nestedGraphDeclaration]!!.classDeclaration
                    )
                }

                validateForNestedDefaultNavGraphs(
                    rootNavGraphs = rawNavGraph.childNavGraphSpecDeclarations.mapNotNull(rawNavGraphMap::get),
                    rawNavGraphMap = rawNavGraphMap
                )
            }
        }
    }

    /**
     * Checkt ob es pro NavGraph Level GENAU eine Destination / NavGraph gibt, welcher isStart = true gesetzt hat
     */
    @Throws(IllegalStateException::class)
    private fun validateIfStartingParameterIsSetForEachNavGraph(
        rawNavGraphs: List<RawComposeNavGraphInfo>,
        rawNavGraphComponentsMap: Map<KSClassDeclaration, List<RawNavComponentInfo>>
    ) {
        rawNavGraphs.forEach { navGraph ->
            val connectedRawComponents = rawNavGraphComponentsMap[navGraph.classDeclaration]!!
            val isStartRawComponents = connectedRawComponents.filter(RawNavComponentInfo::isStart)
            val isStartRawComponentsCount = isStartRawComponents.count()
            when {
                isStartRawComponentsCount == 0 -> throw IllegalStateException(
                    "There is no 'isStart' Component defined for the following NavGraph: ${navGraph.classDeclaration} - There has to be EXACTLY ONE NavGraph or Destination with 'isStart' set to true."
                )
                isStartRawComponentsCount > 1 -> throw IllegalStateException(
                    "Multiple 'isStart' Components are defined for the following NavGraph: ${navGraph.classDeclaration} - There has to be EXACTLY ONE NavGraph or Destination which has 'isStart' set to true.\n" +
                            "Components: ${isStartRawComponents.joinToString(", ") { it.classDeclaration.getFileReferenceText() }}"
                )
            }
        }
    }

    /**
     * Checkt ob eine Destination in genau einem oder keinem NavGraph vorhanden ist
     */
    @Throws(IllegalStateException::class)
    private fun validateIfDestinationIsDefinedInExactlyOneNavGraph(rawNavGraphs: List<RawComposeNavGraphInfo>) {
        rawNavGraphs
            .flatMap(RawComposeNavGraphInfo::childNavDestinationSpecDeclarations)
            .groupingBy { it }
            .eachCount()
            .forEach {
                if (it.value == 1) return@forEach
                throw IllegalStateException(
                    "The following Destination is a child of ${it.value} NavGraphs: ${it.key}.\n" +
                            "A Destination has to be the child of exactly ONE NavGraph."
                )
            }
    }

    /**
     * Checkt ob ein NavGraph in genau einem oder keinem NavGraph vorhanden ist
     */
    @Throws(IllegalStateException::class)
    private fun validateIfNavGraphIsDefinedInAtMostOneNavGraph(rawNavGraphs: List<RawComposeNavGraphInfo>) {
        rawNavGraphs
            .flatMap(RawComposeNavGraphInfo::childNavGraphSpecDeclarations)
            .groupingBy { it }
            .eachCount()
            .forEach {
                if (it.value <= 1) return@forEach
                throw IllegalStateException(
                    "The following NavGraph is a child of ${it.value} different NavGraphs: ${it.key}.\n" +
                            "A NavGraph can only have ONE or ZERO parent NavGraphs."
                )
            }
    }

    /**
     * Checkt ob ein End NavGraph (nestedNavGraphs.size == 0) mindestens eine Destination besitzt.
     */
    @Throws(IllegalStateException::class)
    private fun validateIfLeafNavGraphsContainDestination(rawNavGraphs: List<RawComposeNavGraphInfo>) {
        rawNavGraphs.forEach {
            if (it.childNavGraphSpecDeclarations.any() || it.childNavDestinationSpecDeclarations.any()) return@forEach
            throw IllegalStateException("The following NavGraph does not contain any Children: ${it.simpleName}")
        }
    }


    @Throws(IllegalStateException::class)
    private fun validateIfAllRoutesAreUnique(
        rawNavGraphs: List<RawNavComponentInfo>,
        rawNavDestinations: List<RawComposeDestinationInfo>
    ) {
        val routeNames = mutableSetOf<String>()

        rawNavGraphs.forEach {
            if(routeNames.add(it.baseRoute)) return@forEach
            //Duplicate route detected
            throw IllegalStateException("The following NavGraph is identified by a route that is already in use: ${it.classDeclaration} - Route: ${it.baseRoute}")
        }

        rawNavDestinations.forEach {
            if(routeNames.add(it.baseRoute)) return@forEach
            throw IllegalStateException("The following NavDestination is identified by a route that is already in use: ${it.classDeclaration} - Route: ${it.baseRoute}")
        }
    }


    private fun KSClassDeclaration.getNavGraphRouteParameter() = getAnnotationArgument<String>(
        argName = ComposeNavGraphAnnotation.ROUTE_ARG,
        annotation = ComposeNavGraphAnnotation
    ).ifBlank { simpleName.asString() }

    private fun KSClassDeclaration.getNavDestinationRouteParameter(): String = getAnnotationArgument<String>(
        argName = ComposeDestinationAnnotation.ROUTE_ARG,
        annotation = ComposeDestinationAnnotation
    ).ifBlank { simpleName.asString() }

    private fun KSClassDeclaration.getIsDefaultNavGraphParameter() = getAnnotationArgument<Boolean>(
        argName = ComposeNavGraphAnnotation.IS_DEFAULT_NAV_GRAPH,
        annotation = ComposeNavGraphAnnotation
    )

    private fun KSClassDeclaration.getIsStartParameter(annotation: KSClassDeclaration) = getAnnotationArgument<Boolean>(
        argName = "isStart",
        annotationName = annotation.simpleName.asString()
    )
}