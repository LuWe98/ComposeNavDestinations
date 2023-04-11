package com.welu.compose_nav_destinations_ksp.mapper

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.welu.compose_nav_destinations_ksp.extensions.ksp.asImportInfo
import com.welu.compose_nav_destinations_ksp.extensions.ksp.getAnnotationArgument
import com.welu.compose_nav_destinations_ksp.extractor.NavArgsInfoExtractor
import com.welu.compose_nav_destinations_ksp.model.ImportInfo
import com.welu.compose_nav_destinations_ksp.model.components.ComposeNavGraphInfo
import com.welu.compose_nav_destinations_ksp.model.components.rawcomponents.RawComposeDestinationInfo
import com.welu.compose_nav_destinations_ksp.model.components.rawcomponents.RawComposeNavGraphInfo
import com.welu.compose_nav_destinations_ksp.model.navargs.NavArgsInfo
import com.welu.compose_nav_destinations_ksp.utils.ImportUtils

class ComposeNavGraphMapper(
    private val logger: KSPLogger,
    private val navArgsInfoExtractor: NavArgsInfoExtractor
) : NavComponentMapper<RawComposeNavGraphInfo, ComposeNavGraphInfo> {

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
        simpleName = rawNavGraphInfo.simpleName + ImportUtils.NAV_COMPONENT_SPEC_SUFFIX,
        packageDir = ImportUtils.NAV_GRAPH_SPEC_PACKAGE
    )

    private fun mapStartComponentDeclarationToImport(rawNavGraphInfo: RawComposeNavGraphInfo) = rawNavGraphInfo.startComponentDeclaration!!.let { component ->
        when (component) {
            is RawComposeNavGraphInfo -> ImportInfo(
                simpleName = component.simpleName + ImportUtils.NAV_COMPONENT_SPEC_SUFFIX,
                packageDir = ImportUtils.NAV_GRAPH_SPEC_PACKAGE
            )
            is RawComposeDestinationInfo -> {
                component.classDeclaration.asImportInfo(ImportUtils.NAV_COMPONENT_SPEC_SUFFIX)!!
            }
        }
    }

    private fun mapParentNavGraphSpecToImport(rawNavGraphInfo: RawComposeNavGraphInfo) = rawNavGraphInfo.parentNavGraphSpecDeclaration?.let {
        ImportInfo(it.simpleName.asString() + ImportUtils.NAV_COMPONENT_SPEC_SUFFIX, ImportUtils.NAV_GRAPH_SPEC_PACKAGE)
    }

    private fun mapChildNavGraphSpecsToImport(rawNavGraphInfo: RawComposeNavGraphInfo) = rawNavGraphInfo.childNavGraphSpecDeclarations.map {
        ImportInfo(it.simpleName.asString() + ImportUtils.NAV_COMPONENT_SPEC_SUFFIX, ImportUtils.NAV_GRAPH_SPEC_PACKAGE)
    }.toList()

    private fun mapChildNavDestinationSpecsToImport(rawNavGraphInfo: RawComposeNavGraphInfo) = rawNavGraphInfo.childNavDestinationSpecDeclarations.mapNotNull {
        it.asImportInfo(ImportUtils.NAV_COMPONENT_SPEC_SUFFIX)
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
        annotation = com.welu.compose_nav_destinations_ksp.annotations.ComposeNavGraphAnnotation,
        argName = com.welu.compose_nav_destinations_ksp.annotations.ComposeNavGraphAnnotation.ARGS_CLASS_ARG
    ).declaration as KSClassDeclaration
}