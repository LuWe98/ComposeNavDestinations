package com.welu.compose_nav_destinations_ksp.generation

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.squareup.kotlinpoet.ksp.writeTo
import com.welu.compose_nav_destinations_ksp.extensions.ksp.dependencies
import com.welu.compose_nav_destinations_ksp.generation.component.DestinationSpecFileMapper
import com.welu.compose_nav_destinations_ksp.generation.component.NavGraphSpecFileMapper
import com.welu.compose_nav_destinations_ksp.generation.extensions.NavDestinationsExtensionsFileMapper
import com.welu.compose_nav_destinations_ksp.generation.extensions.CustomNavArgsFileMapper
import com.welu.compose_nav_destinations_ksp.generation.extensions.DestinationExtensionsFileMapper
import com.welu.compose_nav_destinations_ksp.model.components.ComposeDestinationInfo
import com.welu.compose_nav_destinations_ksp.model.components.ComposeNavGraphInfo
import com.welu.compose_nav_destinations_ksp.model.components.NavComponentInfo

class FileOutputGenerator(
    private val resolver: Resolver,
    private val logger: KSPLogger,
    private val codeGenerator: CodeGenerator
) {

    fun generate(
        destinations: Sequence<ComposeDestinationInfo>,
        navGraphs: Sequence<ComposeNavGraphInfo>
    ) {

        //Generates the custom NavArgs needed for Navigation
        CustomNavArgsFileMapper.generate(destinations + navGraphs)?.writeTo(
            codeGenerator,
            resolver.dependencies
        )

        //Generates Code for ComposeNavDestinations init
        NavDestinationsExtensionsFileMapper.generate(navGraphs)?.writeTo(
            codeGenerator,
            resolver.dependencies
        )

        //Generates the NavDestinationsExt File
        DestinationExtensionsFileMapper.generate(destinations)?.writeTo(
            codeGenerator,
            resolver.dependencies
        )

        //Generates the DestinationSpecs for all annotated destinations
        destinations.map(DestinationSpecFileMapper::generate).forEach {
            it.writeTo(
                codeGenerator,
                resolver.dependencies
            )
        }

        //Generates the NavGraphSpecs for all annotated NavGraphs
        navGraphs.map(NavGraphSpecFileMapper::generate).forEach {
            it.writeTo(
                codeGenerator,
                resolver.dependencies
            )
        }
    }



    //TODO -> Das vllt mal noch anschauen
//        val commonSuffix = findCommonNavComponentPackageSuffix(navComponents)
//        logger.warn("Common suffix: $commonSuffix")
    private fun findCommonNavComponentPackageSuffix(navComponentInfos: Sequence<NavComponentInfo>): String {

        val commonPrefix = navComponentInfos.map{ it.specImport.qualifiedName }.reduce { currentCommonPrefix, currentPackageName ->

            if(currentCommonPrefix.isEmpty() || currentCommonPrefix == currentPackageName) return@reduce currentCommonPrefix

            var newCommonPrefix = ""
            var lastCommonDotIndex: Int? = null

            for (charIndex in currentCommonPrefix.indices) {
                val currentCommonPrefixChar = currentCommonPrefix[charIndex]
                val currentPackageNameChar = currentPackageName.getOrNull(charIndex)

                if(currentCommonPrefixChar == currentPackageNameChar) {
                    if(charIndex == currentCommonPrefix.lastIndex) {
                        val currentPackageNextChar = currentPackageName.getOrNull(charIndex + 1)
                        if(currentPackageNextChar != null && currentPackageNextChar != '.' && lastCommonDotIndex != null) {
                            newCommonPrefix = newCommonPrefix.substring(0, lastCommonDotIndex)
                        } else {
                            newCommonPrefix += currentCommonPrefixChar
                        }
                    } else {
                        if (currentCommonPrefixChar == '.') lastCommonDotIndex = charIndex
                        newCommonPrefix += currentCommonPrefixChar
                    }

                    continue
                }

//            val currentPackageNextChar = currentPackageName.getOrNull(charIndex + 1)
                if ((currentCommonPrefixChar != '.' && currentPackageNameChar == null) || currentPackageNameChar != '.') {
                    newCommonPrefix = if (lastCommonDotIndex == null) "" else newCommonPrefix.substring(0, lastCommonDotIndex)
                }

                break
            }
            newCommonPrefix
        }.trim()

        return when {
            commonPrefix.isEmpty() -> "Default PackageName"
            commonPrefix.endsWith('.') -> commonPrefix.substring(0, commonPrefix.lastIndex)
            else -> commonPrefix
        }
    }

    //TODO -> Es wurde gemacht: GetCommonPackageNamePart für die Destinations. Dort werden dann die DestinaionsDrin gespeichert.

    /*
        private fun List<DestinationGeneratingParams>.getCommonPackageNamePart(): String {
        var currentCommonPackageName = ""
        map { it.composableQualifiedName }
            .forEachIndexed { idx, packageName ->
                if (idx == 0) {
                    currentCommonPackageName = packageName
                    return@forEachIndexed
                }
                currentCommonPackageName = currentCommonPackageName.commonPrefixWith(packageName)
            }

        if (!currentCommonPackageName.endsWith(".")) {
            currentCommonPackageName = currentCommonPackageName.split(".")
                .dropLast(1)
                .joinToString(".")
        }

        return currentCommonPackageName.removeSuffix(".")
            .ifEmpty {
                throw UnexpectedException(
                    """Unable to get package name for module. Please specify a package name to use in the module's build.gradle file with:"
                    ksp {
                        arg("compose-destinations.codeGenPackageName", "your.preferred.package.name")
                    }
                    And report this issue (with steps to reproduce) if possible.
                """.trimIndent())
            }
    }
     */
}