package com.welu.compose_nav_destinations_ksp.generation

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.squareup.kotlinpoet.ksp.writeTo
import com.welu.compose_nav_destinations_ksp.generation.component.DestinationSpecFileMapper
import com.welu.compose_nav_destinations_ksp.generation.component.NavGraphSpecFileMapper
import com.welu.compose_nav_destinations_ksp.generation.extensions.CustomNavArgsFileMapper
import com.welu.compose_nav_destinations_ksp.generation.extensions.DestinationExtensionsFileMapper
import com.welu.compose_nav_destinations_ksp.generation.extensions.NavDestinationsExtensionsFileMapper
import com.welu.compose_nav_destinations_ksp.model.components.ComposeDestinationInfo
import com.welu.compose_nav_destinations_ksp.model.components.ComposeNavGraphInfo

class FileGenerator(
    private val dependencies: Dependencies,
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) {

    fun generate(
        destinations: List<ComposeDestinationInfo>,
        navGraphs: List<ComposeNavGraphInfo>
    ) {

        //Generates the custom NavArgs needed for Navigation
        CustomNavArgsFileMapper.generate(destinations + navGraphs)?.writeTo(
            codeGenerator,
            dependencies
        )

        //Generates Code for ComposeNavDestinations init
        NavDestinationsExtensionsFileMapper.generate(navGraphs)?.writeTo(
            codeGenerator,
            dependencies
        )

        //Generates the NavDestinationsExt File
        DestinationExtensionsFileMapper.generate(destinations)?.writeTo(
            codeGenerator,
            dependencies
        )

        //Generates the DestinationSpecs for all annotated destinations
        destinations.map(DestinationSpecFileMapper::generate).forEach { fileSpec ->
            fileSpec.writeTo(
                codeGenerator,
                dependencies
            )
        }

        //Generates the NavGraphSpecs for all annotated NavGraphs
        navGraphs.map(NavGraphSpecFileMapper::generate).forEach { fileSpec ->
            fileSpec.writeTo(
                codeGenerator,
                dependencies
            )
        }
    }
}