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
import com.welu.composenavdestinations.extensions.ksp.getNavDestinationDefinitionsOfNavGraph
import com.welu.composenavdestinations.extensions.ksp.getNavGraphDefinitions
import com.welu.composenavdestinations.extensions.write
import com.welu.composenavdestinations.extensions.writeLine
import com.welu.composenavdestinations.generation.ContentGenerator
import com.welu.composenavdestinations.mapper.MapperNavDestinations
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
        if(definitions.none()) return emptyList()
        if(definitions.any { it.classKind != ClassKind.OBJECT }) {
            throw IllegalStateException("Illegal Classkind!")
        }

        val mapper = MapperNavDestinations(resolver, logger)
        val navDestinationInfos = definitions.map(mapper::map)

        NavDestinationValidator.validate(navDestinationInfos)

        ContentGenerator(resolver, codeGenerator).generate(navDestinationInfos)

        _debugFile = codeGenerator.createNewFile(
            dependencies = resolver.dependencies,
            packageName = PackageUtils.PACKAGE_NAME,
            fileName = "LoggingFile"
        )

        annotationTests(resolver)

        _debugFile?.close()
        _debugFile = null

        return definitions.filterNot(KSClassDeclaration::validate).toList()
    }


    //TODO
    // -> Es muss bei jedem NavGraph genau bei einer Destination "isStart" ausgewählt werden!
    // -> Eine Destination ohne Annotation ist automatisch teil vom RootNavGraphen
    // -> Eine Destination darf nur Teil von einem NavGraphen sein
    // -> Extension Functions einbauen, dass man easy mit den generated NavGraph Klassen zeug erledigen kann
    private fun annotationTests(resolver: Resolver) {
        return

        val navGraphs = resolver.getNavGraphDefinitions()
        navGraphs.forEach { navGraphAnnotation ->
            debugFile.writeLine("CUSTOM NAV GRAPH: " + navGraphAnnotation.simpleName.asString())

            //TODO -> Das holen um zu checken ob es die Variable gibt oder nicht. Noch schreiben, dass
            val first = navGraphAnnotation.primaryConstructor!!.parameters.first()
            debugFile.writeLine("First Parameter: " + first.name!!.asString())

            val annotatedDestinations = resolver.getNavDestinationDefinitionsOfNavGraph(navGraphAnnotation.qualifiedName!!.asString())
            annotatedDestinations.forEach { destination ->
                debugFile.writeLine("Destination: " + destination.simpleName.asString())
            }
            debugFile.write("\n")
        }
    }
}