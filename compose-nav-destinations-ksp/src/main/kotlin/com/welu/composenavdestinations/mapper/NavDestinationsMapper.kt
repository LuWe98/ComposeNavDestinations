package com.welu.composenavdestinations.mapper

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.welu.composenavdestinations.extensions.ksp.asImportInfo
import com.welu.composenavdestinations.extractor.NavArgsInfoExtractor
import com.welu.composenavdestinations.model.ImportInfo
import com.welu.composenavdestinations.model.NavArgsInfo
import com.welu.composenavdestinations.model.components.NavDestinationInfo
import com.welu.composenavdestinations.model.rawcomponents.RawNavDestinationInfo
import com.welu.composenavdestinations.utils.PackageUtils

//TODO -> Man muss nur noch deep Links einbauen, kann man auch als extra Extractor wieder machen
class NavDestinationsMapper(
    private val resolver: Resolver,
    private val logger: KSPLogger,
    private val navArgsInfoExtractor: NavArgsInfoExtractor
) : ComponentsMapper<RawNavDestinationInfo, NavDestinationInfo> {

    override fun map(component: RawNavDestinationInfo) = NavDestinationInfo(
        baseRoute = component.baseRoute,
        destinationImport = component.classDeclaration.asImportInfo()!!,
        specImport = component.classDeclaration.asImportInfo(PackageUtils.NAV_COMPONENT_SPEC_SUFFIX)!!,
        parentNavGraphSpecImport = mapParentNavGraphSpecDeclarationToImport(component),
        navArgsInfo = extractNavArgs(component),
        deepLinks = extractDeepLinks(component)
    )

    private fun mapParentNavGraphSpecDeclarationToImport(rawNavDestination: RawNavDestinationInfo) = ImportInfo(
        simpleName = rawNavDestination.parentNavGraph.simpleName.asString() + PackageUtils.NAV_COMPONENT_SPEC_SUFFIX,
        packageDir = PackageUtils.NAV_GRAPH_SPEC_PACKAGE
    )

    private fun extractNavArgs(rawNavDestination: RawNavDestinationInfo): NavArgsInfo? = navArgsInfoExtractor.extractNavArgsClassDeclarationOfNavDestination(
        navDestinationClassDeclaration = rawNavDestination.classDeclaration
    )?.let(navArgsInfoExtractor::extract)

    //TODO -> noch einbauen
    private fun extractDeepLinks(rawNavDestination: RawNavDestinationInfo): List<String> {
        return emptyList()
    }


    /*
     component.toNavDestinationInfo(
    navArgsInfo = navArgsInfoExtractor.extractNavArgsClassDeclarationOfNavDestination(component.classDeclaration)?.let(navArgsInfoExtractor::extract)
)
     */

//    fun toNavDestinationInfo(
//        navArgsInfo: NavArgsInfo? = null,
//        route: String = baseRoute,
//        destinationImport: ImportInfo = classDeclaration.asImportInfo()!!,
//        destinationSpecImport: ImportInfo = classDeclaration.asImportInfo(PackageUtils.NAV_COMPONENT_SPEC_SUFFIX)!!,
//        parentNavGraphSpecImport: ImportInfo = ImportInfo(parentNavGraph.simpleName.asString() + PackageUtils.NAV_COMPONENT_SPEC_SUFFIX, PackageUtils.NAV_GRAPH_SPEC_PACKAGE)
//    ) = NavDestinationInfo(
//        baseRoute = route,
//        destinationImport = destinationImport,
//        destinationSpecImport = destinationSpecImport,
//        parentNavGraphSpecImport = parentNavGraphSpecImport,
//        navArgsInfo = navArgsInfo
//    )

}