package com.welu.composenavdestinations.mapper

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration import com.welu.composenavdestinations.annotations.NavDestinationDefinitionAnnotation
import com.welu.composenavdestinations.annotations.NavGraphDefaultDefinitionAnnotation
import com.welu.composenavdestinations.annotations.NavGraphDefinitionAnnotation
import com.welu.composenavdestinations.extensions.ksp.*
import com.welu.composenavdestinations.extensions.writeLine
import com.welu.composenavdestinations.model.structure.NavDestinationStructure
import com.welu.composenavdestinations.model.structure.NavGraphStructure
import com.welu.composenavdestinations.model.structure.NavigationStructure
import com.welu.composenavdestinations.processors.NavDestinationsProcessor.Companion.debugFile


//TODO -> Eine sealed Class bauen, welche hier als Ergebnis zurückgegeben wird. Mit Fehlermeldungen etc und den benötigten Objekten. Dadurch muss man das nicht hier in der Datei definieren und
// kann die ganuen Meldungen zentral verwalten.
class MapperNavigationStructure(
    private val resolver: Resolver
) {

    fun extractNavigationStructure(navDestinations: Sequence<KSClassDeclaration>): NavigationStructure {

        val defaultNavGraphAnnotation: KSClassDeclaration? = getDefaultNavGraphAnnotationDeclaration()

        val navGraphAnnotations = resolver.getNavGraphDefinitions().let {
            if (defaultNavGraphAnnotation == null) it
            else it + defaultNavGraphAnnotation
        }

        checkUniqueNavGraphNames(navGraphAnnotations)

        checkContainsIsStartParameter(navGraphAnnotations)

        val flatNavGraphDescriptions = extractFlatNavGraphStructures(navGraphAnnotations).also {
            checkIfDestinationIsDefinedInOnlyOneNavGraph(it)
            checkIfNavGraphIsDefinedInOnlyOneNavGraph(it)
            checkIfNavGraphContainsDestination(it)
            checkForCircularDependencies(it)
        }


        val rootNavGraphs = findRootNavGraphs(flatNavGraphDescriptions)

        val rootNavGraphHierarchies = connectSubGraphHierarchies(navGraphs = flatNavGraphDescriptions, currentGraphs = rootNavGraphs).also {
            checkForNestedDefaultNavGraphs(it)
        }

        val defaultNavGraphs = rootNavGraphHierarchies.filter(NavGraphStructure::isDefaultNavGraph).also {
            checkIfDefaultNavGraphIsDefined(it)
        }

        val defaultNavGraph = defaultNavGraphs.first()

        val rootDestinations = extractRootDestinations(navDestinations, flatNavGraphDescriptions)

        val navigationStructure = NavigationStructure(
            rootNavGraphHierarchies.map { navGraph ->
                if (navGraph.classDeclaration == defaultNavGraph.classDeclaration) {
                    navGraph.copy(navDestinations = navGraph.navDestinations + rootDestinations)
                } else {
                    navGraph
                }
            }
        )

        checkIfStartingParameterIsSetForEachNavGraph(navigationStructure.navGraphs)

        rootDestinations.forEach {
            debugFile.writeLine("Root Destination: " + it.classDeclaration)
        }

        navigationStructure.navGraphs.forEach {
            debugFile.writeLine("Root NavGraph: $it")
        }

        return navigationStructure
    }

    /**
     * Returns the [KSClassDeclaration] of the DefaultNavHost
     */
    private fun getDefaultNavGraphAnnotationDeclaration(): KSClassDeclaration? {
        //TODO -> Das schaut, welche Components mit dem DefaultNavGraph annotiert wurden.
        // Wenn irgendwas damit annotiert wurde dann ist dieser Graph auch automatisch der Default NavGraph. Es darf dann keinen anderen NavGraph mit isDefault = true geben!
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
    private fun extractFlatNavGraphStructures(navGraphAnnotations: Sequence<KSClassDeclaration>) = navGraphAnnotations.map { navGraphAnnotation ->

        val annotatedComponents = resolver.findAnnotatedClassesWith(navGraphAnnotation.qualifiedName!!.asString())

        val annotatedGraphs = annotatedComponents.filter(navGraphAnnotations::contains)

        val annotatedDestinations = annotatedComponents.filter { it.isAnnotationPresentSimple(NavDestinationDefinitionAnnotation.import) }

        val invalidComponents = annotatedComponents - annotatedGraphs.toSet() - annotatedDestinations.toSet()

        if (invalidComponents.any()) {
            throw IllegalStateException(
                "Unknown Components have been annotated with ${navGraphAnnotation.qualifiedName?.asString()}." +
                        "Only NavGraphs and NavDestinations can be annotated with such an Annotation. \n" +
                        "Unrecognized components: ${invalidComponents.joinToString(", ") { it.qualifiedName?.asString() ?: it.simpleName.asString() }}"
            )
        }

        NavGraphStructure(
            classDeclaration = navGraphAnnotation,
            route = navGraphAnnotation.getRouteParameter(),
            isDefaultNavGraph = navGraphAnnotation.getIsDefaultNavGraphParameter(),
            isStart = false,
            navDestinations = annotatedDestinations.map {
                NavDestinationStructure(
                    classDeclaration = it,
                    isStart = it.getIsStartParameter(navGraphAnnotation)
                )
            }.toList(),
            nestedNavGraphs = annotatedGraphs.map {
                NavGraphStructure(
                    classDeclaration = it,
                    isStart = it.getIsStartParameter(navGraphAnnotation)
                )
            }.toList()
        )
    }

    /**
     * Extracts all root Destinations which were not explicitly added to a NavGraph.
     *
     * They will be added to the DefaultNavGraphSpec or to a custom NavGraphSpec which has the "default" (default = true) parameter set tot true
     */
    private fun extractRootDestinations(
        navDestinations: Sequence<KSClassDeclaration>,
        flatNavGraphDescriptions: Sequence<NavGraphStructure>
    ): Sequence<NavDestinationStructure> = navDestinations.filter { destination ->
        flatNavGraphDescriptions.none { navGraph ->
            navGraph.navDestinations.any { destination == it.classDeclaration }
        }
    }.map {
        NavDestinationStructure(
            classDeclaration = it
        )
    }

    /**
     * Das sind die NavGraphs, die in keinem anderen NavGraph enthalten sind und somit die Navigation Root bilden
     */
    private fun findRootNavGraphs(flatNavGraphDescriptions: Sequence<NavGraphStructure>) = flatNavGraphDescriptions.filter { currentGraphs ->
        flatNavGraphDescriptions.none { topLevelNavGraph ->
            topLevelNavGraph.nestedNavGraphs.any { nestedNavGraph ->
                nestedNavGraph.classDeclaration == currentGraphs.classDeclaration
            }
        }
    }

    /**
     * Fügt den [currentGraphs] die [NavGraphStructure] des nestedNavGraph parameters hinzu
     */
    private fun connectSubGraphHierarchies(
        navGraphs: Sequence<NavGraphStructure>,
        currentGraphs: Sequence<NavGraphStructure>
    ): Iterable<NavGraphStructure> = currentGraphs.map { currentNavGraph ->
        val nestedNavGraphs = navGraphs.filter { navGraph ->
            currentNavGraph.nestedNavGraphs.any { nestedNavGraph ->
                nestedNavGraph.classDeclaration == navGraph.classDeclaration
            }
        }

        if (nestedNavGraphs.none()) return@map currentNavGraph

        val adjustedNavGraphs = connectSubGraphHierarchies(
            navGraphs = navGraphs - nestedNavGraphs.toSet(),
            currentGraphs = nestedNavGraphs
        ).map { navGraphToCopy ->
            navGraphToCopy.copy(
                isStart = currentNavGraph.nestedNavGraphs.first { navGraphToCopy.classDeclaration == it.classDeclaration }.isStart
            )
        }

        currentNavGraph.copy(nestedNavGraphs = adjustedNavGraphs)
    }.asIterable()


    //TODO -> Das eventuell auch noch machen für Destinations und NavGraphs aber von dem
    /**
     * Checkt ob es NavGraphDefinitions mit dem gleichen Namen gibt
     */
    @Throws(IllegalStateException::class)
    private fun checkUniqueNavGraphNames(navGraphAnnotations: Sequence<KSClassDeclaration>) {
        if (navGraphAnnotations.distinctBy { it.simpleName.asString() }.count() == navGraphAnnotations.count()) return
        val navGraphNameDeclarationMap = navGraphAnnotations.groupBy { it.simpleName.asString() }.filter { it.value.size != 1 }
        throw IllegalStateException(
            "NavGraphs with same name are present: ${navGraphNameDeclarationMap.keys.joinToString(", ") { it }}"
        )
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
    private fun checkForCircularDependencies(flatNavGraphDescriptions: Sequence<NavGraphStructure>) {
        val flatNavGraphDescriptionLookupMap = flatNavGraphDescriptions.associateBy { it.classDeclaration }

        fun checkNavGraphForCircularDependencies(
            navGraph: NavGraphStructure,
            visitedNodeNamesTop: MutableList<KSClassDeclaration>
        ) {
            if (visitedNodeNamesTop.contains(navGraph.classDeclaration)) {
                throw IllegalStateException("Circular dependency detected in the following NavGraph: ${navGraph.simpleName} - Circular dependencies between the following NavGraphs: $visitedNodeNamesTop")
            }

            visitedNodeNamesTop.add(navGraph.classDeclaration)

            navGraph.nestedNavGraphs.forEach { nestedGraph ->
                checkNavGraphForCircularDependencies(flatNavGraphDescriptionLookupMap[nestedGraph.classDeclaration]!!, visitedNodeNamesTop)
            }
        }

        flatNavGraphDescriptions.forEach { navGraph ->
            checkNavGraphForCircularDependencies(navGraph, mutableListOf())
        }
    }

    /**
     * Checks if a default NavGraph is defined in the Hierarchy
     */
    @Throws(IllegalStateException::class)
    private fun checkIfDefaultNavGraphIsDefined(defaultNavGraphs: List<NavGraphStructure>) {
        if (defaultNavGraphs.isEmpty()) {
            throw IllegalStateException(
                "There is no valid default NavGraphDefinition defined. Change 'isDefault = true' in one of your root NavGraphs " +
                        "OR annotate the starting Destination or NavGraph with @DefaultNavGraph(isStart = true)"
            )
        }

        if (defaultNavGraphs.size > 1) {
            throw IllegalStateException(
                "There are multiple default NavGraphDefinitions present: ${defaultNavGraphs.joinToString(", ") { it.simpleName }}"
            )
        }
    }

    /**
     * Checkt ob es in der Hierarchie eines NavGraphs einen  NavGraph mit isDefaultNavGraph = true gibt
     */
    @Throws(IllegalStateException::class)
    private fun checkForNestedDefaultNavGraphs(rootNavGraphHierarchy: Iterable<NavGraphStructure>) {
        rootNavGraphHierarchy.forEach {
            it.nestedNavGraphs.forEach { nestedGraph ->
                if (nestedGraph.isDefaultNavGraph) {
                    throw IllegalStateException(
                        "Nested NavGraphs cannot be the default NavGraph of an App. " +
                                "Set 'isDefaultNavGraph = false' on the following NavGraph Annotation: " + nestedGraph.simpleName
                    )
                } else {
                    checkForNestedDefaultNavGraphs(it.nestedNavGraphs)
                }
            }
        }
    }

    /**
     * Checkt ob es pro NavGraph Level GENAU eine Destination / NavGraph gibt, welcher isStart = true gesetzt hat
     */
    @Throws(IllegalStateException::class)
    private fun checkIfStartingParameterIsSetForEachNavGraph(rootNavGraphHierarchy: Iterable<NavGraphStructure>) {
        rootNavGraphHierarchy.forEach { navGraph ->
            val navDestinationsIsStart = navGraph.navDestinations.filter(NavDestinationStructure::isStart)
            val navGraphsIsStart = navGraph.nestedNavGraphs.filter(NavGraphStructure::isStart)
            when (navDestinationsIsStart.size + navGraphsIsStart.size) {
                1 -> checkIfStartingParameterIsSetForEachNavGraph(navGraph.nestedNavGraphs)
                0 -> throw IllegalStateException(
                    "There is no 'isStart' Component defined for the following NavGraph: ${navGraph.simpleName} - There has to be EXACTLY ONE NavGraph or Destination with 'isStart' set to true."
                )
                else -> throw IllegalStateException(
                    "Multiple 'isStart' Components are defined for the following NavGraph: ${navGraph.simpleName} - There has to be EXACTLY ONE NavGraph or Destination which has 'isStart' set to true.\n" +
                            "Destinations: ${navDestinationsIsStart.joinToString(", ") { it.simpleName }} | NavGraphs: ${navGraphsIsStart.joinToString(",") { it.simpleName }}"
                )
            }
        }
    }

    /**
     * Checkt ob eine Destination in genau einem oder keinem NavGraph vorhanden ist
     */
    @Throws(IllegalStateException::class)
    private fun checkIfDestinationIsDefinedInOnlyOneNavGraph(flatNavGraphDescriptions: Sequence<NavGraphStructure>) {
        flatNavGraphDescriptions
            .flatMap(NavGraphStructure::navDestinations)
            .groupBy(NavDestinationStructure::classDeclaration)
            .forEach {
                if (it.value.size > 1) {
                    throw IllegalStateException(
                        "The following Destination is a child of ${it.value.size} different NavGraphs: ${it.key}.\n" +
                                "A Destination can only have a single parent NavGraph."
                    )
                }
            }
    }

    /**
     * Checkt ob ein NavGraph in genau einem oder keinem NavGraph vorhanden ist
     */
    @Throws(IllegalStateException::class)
    private fun checkIfNavGraphIsDefinedInOnlyOneNavGraph(flatNavGraphDescriptions: Sequence<NavGraphStructure>) {
        flatNavGraphDescriptions
            .flatMap(NavGraphStructure::nestedNavGraphs)
            .groupBy(NavGraphStructure::classDeclaration)
            .forEach {
                if (it.value.size > 1) {
                    throw IllegalStateException(
                        "The following NavGraph is a child of ${it.value.size} different NavGraphs: ${it.key}.\n" +
                                "A NavGraph can only have a single parent NavGraph."
                    )
                }
            }
    }

    /**
     * Checkt ob ein End NavGraph (nestedNavGraphs.size == 0) mindestens eine Destination besitzt.
     */
    @Throws(IllegalStateException::class)
    private fun checkIfNavGraphContainsDestination(flatNavGraphDescriptions: Sequence<NavGraphStructure>) {
        flatNavGraphDescriptions.forEach {
            if (it.nestedNavGraphs.none() && it.navDestinations.none()) {
                throw IllegalStateException(
                    "The following NavGraph does not contain any Children: ${it.simpleName}"
                )
            }
        }
    }


    private fun KSClassDeclaration.getRouteParameter() = getAnnotationArgument<String>(
        argName = NavGraphDefinitionAnnotation.ROUTE_ARG,
        annotation = NavGraphDefinitionAnnotation
    ).ifBlank { simpleName.asString() }

    private fun KSClassDeclaration.getIsDefaultNavGraphParameter() = getAnnotationArgument<Boolean>(
        argName = NavGraphDefinitionAnnotation.IS_DEFAULT_NAV_GRAPH,
        annotation = NavGraphDefinitionAnnotation
    )

    private fun KSClassDeclaration.getIsStartParameter(annotation: KSClassDeclaration) = getAnnotationArgument<Boolean>(
        argName = NavGraphDefaultDefinitionAnnotation.IS_START_ARG,
        annotationName = annotation.simpleName.asString()
    )

}