package com.welu.composenavdestinations.mapper

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.welu.composenavdestinations.annotations.ComposeNavGraphAnnotation
import com.welu.composenavdestinations.extensions.ksp.asImportInfo
import com.welu.composenavdestinations.extensions.ksp.getAnnotationArgument
import com.welu.composenavdestinations.extractor.NavArgsInfoExtractor
import com.welu.composenavdestinations.model.ImportInfo
import com.welu.composenavdestinations.model.navargs.NavArgsInfo
import com.welu.composenavdestinations.model.components.ComposeNavGraphInfo
import com.welu.composenavdestinations.model.components.rawcomponents.RawComposeDestinationInfo
import com.welu.composenavdestinations.model.components.rawcomponents.RawComposeNavGraphInfo
import com.welu.composenavdestinations.utils.PackageUtils

class NavGraphsMapper(
    private val resolver: Resolver,
    private val logger: KSPLogger,
    private val navArgsInfoExtractor: NavArgsInfoExtractor
) : ComponentsMapper<RawComposeNavGraphInfo, ComposeNavGraphInfo> {

    override fun map(component: RawComposeNavGraphInfo) = ComposeNavGraphInfo(
        deepLinks = extractDeepLinks(component),
        navArgsInfo = extractNavArgs(component),
        baseRoute = component.baseRoute,
        specImport = mapNavGraphSpecDeclarationToImport(component),
        startComponentDeclaration = mapStartComponentDeclarationToImport(component),
        parentNavGraphSpecImport = mapParentNavGraphSpecToImport(component),
        childDestinationSpecImports = mapChildNavDestinationSpecsToImport(component),
        childNavGraphSpecImports = mapChildNavGraphSpecsToImport(component)
    )

    private fun mapNavGraphSpecDeclarationToImport(rawNavGraphInfo: RawComposeNavGraphInfo) = ImportInfo(
        simpleName = rawNavGraphInfo.simpleName + PackageUtils.NAV_COMPONENT_SPEC_SUFFIX,
        packageDir = PackageUtils.NAV_GRAPH_SPEC_PACKAGE
    )

    private fun mapStartComponentDeclarationToImport(rawNavGraphInfo: RawComposeNavGraphInfo) = rawNavGraphInfo.startComponentDeclaration!!.let { component ->
        when (component) {
            is RawComposeNavGraphInfo -> ImportInfo(
                simpleName = component.simpleName + PackageUtils.NAV_COMPONENT_SPEC_SUFFIX,
                packageDir = PackageUtils.NAV_GRAPH_SPEC_PACKAGE
            )
            is RawComposeDestinationInfo -> {
                component.classDeclaration.asImportInfo(PackageUtils.NAV_COMPONENT_SPEC_SUFFIX)!!
            }
        }
    }

    private fun mapParentNavGraphSpecToImport(rawNavGraphInfo: RawComposeNavGraphInfo) = rawNavGraphInfo.parentNavGraphSpecDeclaration?.let {
        ImportInfo(it.simpleName.asString() + PackageUtils.NAV_COMPONENT_SPEC_SUFFIX, PackageUtils.NAV_GRAPH_SPEC_PACKAGE)
    }

    private fun mapChildNavGraphSpecsToImport(rawNavGraphInfo: RawComposeNavGraphInfo) = rawNavGraphInfo.childNavGraphSpecDeclarations.map {
        ImportInfo(it.simpleName.asString() + PackageUtils.NAV_COMPONENT_SPEC_SUFFIX, PackageUtils.NAV_GRAPH_SPEC_PACKAGE)
    }.toList()

    private fun mapChildNavDestinationSpecsToImport(rawNavGraphInfo: RawComposeNavGraphInfo) = rawNavGraphInfo.childNavDestinationSpecDeclarations.mapNotNull {
        it.asImportInfo(PackageUtils.NAV_COMPONENT_SPEC_SUFFIX)
    }.toList()


    //TODO -> Noch einbauen -> NavArgs kommen aus einem Parameter von der NavGraphDefinition Annotation
    // -> Man kann dadurch direkt navArgsInfoExtractor.extract mit der KClassDeclaration aufrufen
    private fun extractNavArgs(component: RawComposeNavGraphInfo): NavArgsInfo? {
        val navArgsClass = component.classDeclaration.getNavArgsClassDeclaration()
        if ((navArgsClass.qualifiedName?.asString() ?: Unit::class.qualifiedName) == Unit::class.qualifiedName) return null
        return navArgsInfoExtractor.extract(navArgsClass)
    }

    //TODO -> noch einbauen
    private fun extractDeepLinks(component: RawComposeNavGraphInfo): List<String> {
        return emptyList()
    }

    private fun KSClassDeclaration.getNavArgsClassDeclaration() = getAnnotationArgument<KSType>(
        annotation = ComposeNavGraphAnnotation,
        argName = ComposeNavGraphAnnotation.ARGS_CLASS_ARG
    ).declaration as KSClassDeclaration
}