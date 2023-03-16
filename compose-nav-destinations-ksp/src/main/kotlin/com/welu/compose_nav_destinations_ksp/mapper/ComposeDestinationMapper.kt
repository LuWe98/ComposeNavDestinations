package com.welu.compose_nav_destinations_ksp.mapper

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSType
import com.welu.compose_nav_destinations_ksp.extensions.ksp.asImportInfo
import com.welu.compose_nav_destinations_ksp.extractor.NavArgsInfoExtractor
import com.welu.compose_nav_destinations_ksp.model.ImportInfo
import com.welu.compose_nav_destinations_ksp.model.components.ComposeDestinationInfo
import com.welu.compose_nav_destinations_ksp.model.components.ComposeDestinationType
import com.welu.compose_nav_destinations_ksp.model.components.rawcomponents.RawComposeDestinationInfo
import com.welu.compose_nav_destinations_ksp.model.navargs.NavArgsInfo
import com.welu.compose_nav_destinations_ksp.utils.ImportUtils

//TODO -> Man muss nur noch deep Links einbauen, kann man auch als extra Extractor wieder machen
class ComposeDestinationMapper(
    private val resolver: Resolver,
    private val logger: KSPLogger,
    private val navArgsInfoExtractor: NavArgsInfoExtractor
) : NavComponentMapper<RawComposeDestinationInfo, ComposeDestinationInfo> {

    override fun map(component: RawComposeDestinationInfo): ComposeDestinationInfo {

        val destinationClassSupertype: KSType = extractNavDestinationSupertype(component)
        val destinationType = ComposeDestinationType.fromDestinationSuperType(destinationClassSupertype)

        return ComposeDestinationInfo(
            baseRoute = component.baseRoute,
            destinationImport = component.classDeclaration.asImportInfo()!!,
            specImport = component.classDeclaration.asImportInfo(ImportUtils.NAV_COMPONENT_SPEC_SUFFIX)!!,
            parentNavGraphSpecImport = mapParentNavGraphSpecDeclarationToImport(component),
            destinationType = destinationType,
            navArgsInfo = extractNavArgs(destinationClassSupertype, destinationType),
            deepLinks = extractDeepLinks(component)
        )
    }

    private fun mapParentNavGraphSpecDeclarationToImport(rawNavDestination: RawComposeDestinationInfo) = ImportInfo(
        simpleName = rawNavDestination.parentNavGraph.simpleName.asString() + ImportUtils.NAV_COMPONENT_SPEC_SUFFIX,
        packageDir = ImportUtils.NAV_GRAPH_SPEC_PACKAGE
    )

    private fun extractNavDestinationSupertype(component: RawComposeDestinationInfo): KSType = component
        .classDeclaration
        .superTypes
        .firstOrNull()
        ?.resolve() ?: throw IllegalStateException("Destination does not implement a valid ComposeDestination interface.")

    private fun extractNavArgs(
        destinationClassSupertype: KSType,
        destinationType: ComposeDestinationType
    ): NavArgsInfo? = navArgsInfoExtractor.extractNavArgsClassDeclarationWith(
        destinationClassSupertype = destinationClassSupertype,
        destinationType = destinationType
    )?.let(navArgsInfoExtractor::extract)

    //TODO -> noch einbauen
    private fun extractDeepLinks(rawNavDestination: RawComposeDestinationInfo): List<String> {
        return emptyList()
    }

}