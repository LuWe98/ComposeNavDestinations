package com.welu.composenavdestinations.generation.component

import com.welu.composenavdestinations.extensions.div
import com.welu.composenavdestinations.generation.FileContentInfoGenerator
import com.welu.composenavdestinations.generation.templates.NavDestinationCodeTemplates
import com.welu.composenavdestinations.generation.templates.NavGraphCodeTemplates
import com.welu.composenavdestinations.model.*
import com.welu.composenavdestinations.model.components.NavGraphInfo
import com.welu.composenavdestinations.utils.PackageUtils

//TODO -> noch einbauen
object FileGeneratorNavGraphSpec : FileContentInfoGenerator<NavGraphInfo> {

    override fun generate(instance: NavGraphInfo): FileContentInfo = if (instance.isArgNavGraph) {
        generateArgnNavGraphSpec(instance)
    } else {
        generatePlainNavGraphSpec(instance)
    }

    private fun generatePlainNavGraphSpec(plainNavGraphInfo: NavGraphInfo) = FileContentInfo(
        fileName = plainNavGraphInfo.simpleName,
        packageDir = PackageUtils.NAV_GRAPH_SPEC_PACKAGE,
        imports = mutableSetOf(
            PackageUtils.ANDROID_NAVIGATION_DEEP_LINK_IMPORT,
            PackageUtils.NAV_COMPONENT_SPEC_IMPORT,
            PackageUtils.NAV_GRAPH_PLAIN_SPEC_IMPORT,
            PackageUtils.NAV_GRAPH_SPEC_IMPORT
        ).apply {
            addAll(plainNavGraphInfo.allImports)
        },
        code = NavGraphCodeTemplates.NAV_GRAPH_PLAIN_SPEC_TEMPLATE
            .replace(
                NavGraphCodeTemplates.PLACEHOLDER_NAV_GRAPH_SPEC_NAME,
                plainNavGraphInfo.specImport.simpleName
            ).replace(
                NavGraphCodeTemplates.PLACEHOLDER_NAV_GRAPH_SPEC_BASE_ROUTE,
                plainNavGraphInfo.baseRoute
            ).replace(
                NavGraphCodeTemplates.PLACEHOLDER_NAV_GRAPH_SPEC_PARENT_NAV_GRAPH_SPEC,
                plainNavGraphInfo.parentNavGraphSpecImport?.simpleName ?: "null"
            ).replace(
                NavGraphCodeTemplates.PLACEHOLDER_NAV_GRAPH_SPEC_START_COMPONENT,
                plainNavGraphInfo.startComponentDeclaration.simpleName
            ).replace(
                NavGraphCodeTemplates.PLACEHOLDER_NAV_GRAPH_SPEC_CHILD_NAV_COMPONENT_SPECS,
                generateChildNavComponentSpecParams(plainNavGraphInfo)
            )
    )

    private fun generateChildNavComponentSpecParams(navGraphInfo: NavGraphInfo): String = navGraphInfo
        .allChildNavComponentSpecImports
        .joinToString(",\n\t\t", transform = ImportInfo::simpleName)


    //TODO -> Das machen
    private fun generateArgnNavGraphSpec(argNavGraphSpec: NavGraphInfo): FileContentInfo {
        val sortedParams = argNavGraphSpec
            .navArgsInfo!!
            .parameters
            .sortedWith(compareBy(Parameter::typeInfo / ParameterTypeInfo::isNullable, Parameter::hasDefaultValue))

        return FileContentInfo(
            fileName = argNavGraphSpec.simpleName,
            packageDir = PackageUtils.NAV_GRAPH_SPEC_PACKAGE,
            imports = mutableSetOf(
                PackageUtils.ANDROID_NAVIGATION_DEEP_LINK_IMPORT,
                PackageUtils.NAV_COMPONENT_SPEC_IMPORT,
                PackageUtils.NAV_GRAPH_SPEC_ARG_IMPORT,
                PackageUtils.NAV_GRAPH_SPEC_IMPORT,
                PackageUtils.NAV_ARGUMENT_IMPORT,
                PackageUtils.ANDROID_NAVIGATION_NAMED_NAV_ARGUMENT_IMPORT,
                PackageUtils.ROUTABLE_IMPORT,
                PackageUtils.ANDROID_NAVIGATION_NAV_BACK_STACK_ENTRY_IMPORT,
            ).apply {
                addAll(argNavGraphSpec.allImports)
            },
            code = NavGraphCodeTemplates.NAV_GRAPH_ARG_SPEC_TEMPLATE
                .replace(
                    NavGraphCodeTemplates.PLACEHOLDER_NAV_GRAPH_SPEC_NAME,
                    argNavGraphSpec.specImport.simpleName
                ).replace(
                    NavGraphCodeTemplates.PLACEHOLDER_NAV_GRAPH_SPEC_BASE_ROUTE,
                    argNavGraphSpec.baseRoute
                ).replace(
                    NavGraphCodeTemplates.PLACEHOLDER_NAV_GRAPH_SPEC_PARENT_NAV_GRAPH_SPEC,
                    argNavGraphSpec.parentNavGraphSpecImport?.simpleName ?: "null"
                ).replace(
                    NavGraphCodeTemplates.PLACEHOLDER_NAV_GRAPH_SPEC_START_COMPONENT,
                    argNavGraphSpec.startComponentDeclaration.simpleName
                ).replace(
                    NavGraphCodeTemplates.PLACEHOLDER_NAV_GRAPH_SPEC_CHILD_NAV_COMPONENT_SPECS,
                    generateChildNavComponentSpecParams(argNavGraphSpec)
                ).replace(
                    NavGraphCodeTemplates.PLACEHOLDER_NAV_ARG_SPEC_NAV_ARG_TYPE,
                    argNavGraphSpec.navArgsInfo.name
                ).replace(
                    NavGraphCodeTemplates.PLACEHOLDER_NAV_ARG_SPEC_ROUTE_ARGS,
                    NavArgsGeneratorUtils.generateRoute(sortedParams)
                ).replace(
                    NavGraphCodeTemplates.PLACEHOLDER_NAV_ARG_SPEC_INVOKE_FUNCTION_PARAMETER,
                    NavArgsGeneratorUtils.generateInvokeParameters(argNavGraphSpec)
                ).replace(
                    NavGraphCodeTemplates.PLACEHOLDER_NAV_ARG_SPEC_INVOKE_FUNCTION_BODY,
                    NavArgsGeneratorUtils.generateInvokeBody(argNavGraphSpec.baseRoute, sortedParams)
                ).replace(
                    NavGraphCodeTemplates.PLACEHOLDER_NAV_ARG_SPEC_NAMED_ARGUMENTS,
                    NavArgsGeneratorUtils.generateNamedNavArguments(sortedParams)
                ).replace(
                    NavGraphCodeTemplates.PLACEHOLDER_NAV_ARG_SPEC_GET_ARGS_BACKSTACK,
                    NavArgsGeneratorUtils.generateGetArgsBody(argNavGraphSpec, ArgContainer.NabBackStackEntry)
                ).replace(
                    NavDestinationCodeTemplates.PLACEHOLDER_NAV_SPEC_DEEPLINK_VALUE,
                    "emptyList()"
                )
        )
    }

}