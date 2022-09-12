package com.welu.composenavdestinations.mapper

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.welu.composenavdestinations.extensions.ksp.findAnnotatedClassesWith
import com.welu.composenavdestinations.extensions.ksp.getNavGraphDefinitions
import com.welu.composenavdestinations.extensions.writeLine
import com.welu.composenavdestinations.processors.NavDestinationsProcessor.Companion.debugFile

class MapperNavigationStructure {

    //Das braucht man eigentlich nicht
    data class NavigationStructure(
        val navGraphs: Iterable<NavGraphDescription> = emptyList(),
        val navDestinations: Iterable<NavDestinationDescription> = emptyList()
    )

    data class NavGraphDescription(
        val name: String,
        val nestedNavGraphs: Iterable<NavGraphDescription> = emptyList(),
        val navDestinations: Iterable<NavDestinationDescription> = emptyList()
    )

    data class NavDestinationDescription(
        val name: String
    )

    //TODO
    // -> Es muss bei jedem NavGraph genau bei einer Destination "isStart" ausgewählt werden!
    // -> Eine Destination ohne Annotation ist automatisch teil vom RootNavGraph
    // -> Eine Destination darf nur Teil von einem NavGraphen sein
    // -> Extension Functions einbauen, dass man easy mit den generated NavGraph Klassen zeug erledigen kann
    fun annotationTests(
        resolver: Resolver,
        navDestinations: Sequence<KSClassDeclaration>
    ) {

        val navGraphAnnotations: Sequence<KSClassDeclaration> = resolver.getNavGraphDefinitions()

        if (navGraphAnnotations.distinctBy { it.simpleName.asString() }.count() != navGraphAnnotations.count()) {
            throw IllegalStateException("NavGraphs with same name are present.")
        }

        //TEST Extract value
        navGraphAnnotations.forEach {

        }



        val flatNavGraphDescriptions = extractFlatNavGraphDescriptions(resolver, navGraphAnnotations)

        val rootDestinations = extractRootDestinations(navDestinations, flatNavGraphDescriptions).onEach {
            debugFile.writeLine("Root Destination: " + it.simpleName.asString())
        }

        extractTree(flatNavGraphDescriptions)
    }

    /**
     * Extracts all root Destinations which were not explicitly added to a NavGraph.
     *
     * They will be added to the DefaultNavGraphSpec or to a custom NavGraphSpec which has the "default" (default = true) parameter set tot true
     */
    private fun extractRootDestinations(
        navDestinations: Sequence<KSClassDeclaration>,
        flatNavGraphDescriptions: Sequence<NavGraphDescription>
    ) = navDestinations.filter { dest ->
        flatNavGraphDescriptions.none {
            it.navDestinations.any { desc ->
                dest.simpleName.asString() == desc.name
            }
        }
    }

    private fun extractFlatNavGraphDescriptions(
        resolver: Resolver,
        navGraphAnnotations: Sequence<KSClassDeclaration>
    ) = navGraphAnnotations.map { navGraphAnnotation ->
        //TODO -> Das holen um zu checken ob es die Variable gibt oder nicht. Noch schreiben, dass
        //val first = navGraphAnnotation.primaryConstructor!!.parameters.first()
        //debugFile.writeLine("First Parameter: " + first.name!!.asString())
        //debugFile.writeLine("NavGraph: " + navGraphAnnotation.simpleName.asString())

        val annotatedComponents = resolver.findAnnotatedClassesWith(navGraphAnnotation.qualifiedName!!.asString())

        val annotatedGraphs = annotatedComponents.filter(navGraphAnnotations::contains)

        val annotatedDestinations = annotatedComponents - annotatedGraphs.toSet()

        NavGraphDescription(
            name = navGraphAnnotation.simpleName.asString(),
            navDestinations = annotatedDestinations.map { NavDestinationDescription(it.simpleName.asString()) }.toList(),
            nestedNavGraphs = annotatedGraphs.map { NavGraphDescription(it.simpleName.asString()) }.toList()
        )
    }

    private fun extractTree(flatNavGraphDescriptions: Sequence<NavGraphDescription>) {

        val rootNavGraphs = findRootNavGraphs(flatNavGraphDescriptions)

        val navArgsHierarchy = connectGraphs(flatNavGraphDescriptions, rootNavGraphs)

        navArgsHierarchy.forEach {
            debugFile.writeLine("Connected Graphs: $it")
        }
    }

    /**
     * Das sind die NavGraphs, die in keinem anderen NavGraph enthalten sind und somit die Navigation Root bilden
     */
    private fun findRootNavGraphs(flatNavGraphDescriptions: Sequence<NavGraphDescription>) = flatNavGraphDescriptions.filter { currentGraphs ->
        flatNavGraphDescriptions.none { topLevelNavGraph ->
            topLevelNavGraph.nestedNavGraphs.any { nestedNavGraph ->
                nestedNavGraph.name == currentGraphs.name
            }
        }
    }

    /**
     * Fügt den [currentGraphs] die [NavGraphDescription] des nestedNavGraph parameters hinzu
     */
    private fun connectGraphs(
        navGraphs: Sequence<NavGraphDescription>,
        currentGraphs: Sequence<NavGraphDescription>
    ): List<NavGraphDescription> = currentGraphs.map { currentNavGraph ->
        val nestedNavGraphs = navGraphs.filter { a ->
            currentNavGraph.nestedNavGraphs.any { nestedNavGraph ->
                nestedNavGraph.name == a.name
            }
        }

        if (nestedNavGraphs.none()) return@map currentNavGraph

        currentNavGraph.copy(
            nestedNavGraphs = connectGraphs(
                navGraphs = navGraphs - nestedNavGraphs.toSet(),
                currentGraphs = nestedNavGraphs
            )
        )
    }.toList()

}