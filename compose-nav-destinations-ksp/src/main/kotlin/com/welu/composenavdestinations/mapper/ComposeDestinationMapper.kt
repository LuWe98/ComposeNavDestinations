package com.welu.composenavdestinations.mapper

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSType
import com.welu.composenavdestinations.extensions.ksp.asImportInfo
import com.welu.composenavdestinations.extractor.NavArgsInfoExtractor
import com.welu.composenavdestinations.model.ImportInfo
import com.welu.composenavdestinations.model.navargs.NavArgsInfo
import com.welu.composenavdestinations.model.components.ComposeDestinationInfo
import com.welu.composenavdestinations.model.components.ComposeDestinationType
import com.welu.composenavdestinations.model.components.rawcomponents.RawComposeDestinationInfo
import com.welu.composenavdestinations.utils.PackageUtils

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
            specImport = component.classDeclaration.asImportInfo(PackageUtils.NAV_COMPONENT_SPEC_SUFFIX)!!,
            parentNavGraphSpecImport = mapParentNavGraphSpecDeclarationToImport(component),
            destinationType = destinationType,
            navArgsInfo = extractNavArgs(destinationClassSupertype, destinationType),
            deepLinks = extractDeepLinks(component)
        )
    }

    private fun mapParentNavGraphSpecDeclarationToImport(rawNavDestination: RawComposeDestinationInfo) = ImportInfo(
        simpleName = rawNavDestination.parentNavGraph.simpleName.asString() + PackageUtils.NAV_COMPONENT_SPEC_SUFFIX,
        packageDir = PackageUtils.NAV_GRAPH_SPEC_PACKAGE
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