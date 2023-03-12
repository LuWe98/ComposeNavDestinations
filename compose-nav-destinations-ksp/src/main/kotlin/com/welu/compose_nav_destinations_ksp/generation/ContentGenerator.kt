package com.welu.compose_nav_destinations_ksp.generation

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.welu.compose_nav_destinations_ksp.generation.component.FileGeneratorDestinationExtensions
import com.welu.compose_nav_destinations_ksp.generation.component.FileGeneratorDestinationSpec
import com.welu.compose_nav_destinations_ksp.generation.component.FileGeneratorNavGraphSpec
import com.welu.compose_nav_destinations_ksp.generation.general.FileComposeNavDestinationsExtensions
import com.welu.compose_nav_destinations_ksp.generation.general.FileGeneratorCustomNavArgs
import com.welu.compose_nav_destinations_ksp.model.components.ComposeDestinationInfo
import com.welu.compose_nav_destinations_ksp.model.components.ComposeNavGraphInfo
import com.welu.compose_nav_destinations_ksp.model.components.NavComponentInfo

class ContentGenerator(
    private val resolver: Resolver,
    private val logger: KSPLogger,
    private val codeGenerator: CodeGenerator
) {

    fun generate(
        destinations: Sequence<ComposeDestinationInfo>,
        navGraphs: Sequence<ComposeNavGraphInfo>
    ) {

        val navComponents = destinations + navGraphs

        //TODO -> Das vllt mal noch anschauen
//        val commonSuffix = findCommonNavComponentPackageSuffix(navComponents)
//        logger.warn("Common suffix: $commonSuffix")

        //Hier sollte der Common PackageName übergeben werden
        //In den FileGenerators sollte bei den FileContentInfos nur der additional suffix mitgegeben werden, der an den Common PackageName angehängt wird
        val fileContentInfoOutputWriter = FileContentInfoOutputWriter(
            codeGenerator,
            resolver
        )

        //Generates the custom NavArgs needed for Navigation
        FileGeneratorCustomNavArgs.generate(navComponents)?.let(fileContentInfoOutputWriter::writeFile)

        //Generates the NavDestinationsExt File
        FileGeneratorDestinationExtensions.generate(destinations).let(fileContentInfoOutputWriter::writeFile)

        //Generates Code for ComposeNavDestinations init
        FileComposeNavDestinationsExtensions.generate(navGraphs).let(fileContentInfoOutputWriter::writeFile)

        //Generates the DestinationSpecs for all annotated destinations
        destinations.map(FileGeneratorDestinationSpec::generate).forEach(fileContentInfoOutputWriter::writeFile)

        //Generates the NavGraphSpecs for all annotated NavGraphs
        navGraphs.map(FileGeneratorNavGraphSpec::generate).forEach(fileContentInfoOutputWriter::writeFile)


        //Generates the NavDestinationUtils File
        // TODO -> No longer needed, because the destination will be found in a Service Locator type of way in order for the Methods to be available instantly
        //  Die Files unten braucht man auch nicht mehr generieren und kann stattdessen direkt darauf zugreifen.
        // FileGeneratorNavComponentUtils.generate(navComponents).let(fileContentInfoOutputWriter::writeFile)

        //Generates the NavDestinationsResultExt File
        //FileGeneratorResultExtensions.generate(destinations).let(fileContentInfoOutputWriter::writeFile)

        //Generates the NavControllerExtFile
        //FileGeneratorNavControllerExtensions.generate().let(fileContentInfoOutputWriter::writeFile)

        //Generates the NavBackStackEntryExtFile
        //FileGeneratorNavBackStackEntryExtensions.generate().let(fileContentInfoOutputWriter::writeFile)
    }

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