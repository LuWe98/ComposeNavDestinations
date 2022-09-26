package com.welu.composenavdestinations.generation.component

import com.welu.composenavdestinations.extensions.div
import com.welu.composenavdestinations.generation.FileContentInfoTypedGenerator
import com.welu.composenavdestinations.generation.templates.NavDestinationCodeTemplates
import com.welu.composenavdestinations.generation.templates.NavGraphCodeTemplates
import com.welu.composenavdestinations.model.*
import com.welu.composenavdestinations.model.components.NavGraphInfo
import com.welu.composenavdestinations.utils.PackageUtils

//TODO -> noch einbauen
object FileGeneratorNavGraphSpec : FileContentInfoTypedGenerator<NavGraphInfo> {

    override fun generate(instance: NavGraphInfo): FileContentInfo = if (instance.isArgNavGraph) {
        generateArgnNavGraphSpec(instance)
    } else {
        generatePlainNavGraphSpec(instance)
    }

    private fun generatePlainNavGraphSpec(navGraphInfo: NavGraphInfo) = FileContentInfo(
        fileName = navGraphInfo.simpleName,
        packageDir = PackageUtils.NAV_GRAPH_SPEC_PACKAGE,
        imports = mutableSetOf(
            PackageUtils.ANDROID_NAVIGATION_DEEP_LINK_IMPORT,
            PackageUtils.NAV_COMPONENT_SPEC_IMPORT,
            PackageUtils.NAV_GRAPH_SPEC_IMPORT,
            PackageUtils.NAV_COMPOSE_GRAPH_SPEC_IMPORT
        ).apply {
            addAll(navGraphInfo.allImports)
        },
        code = NavGraphCodeTemplates.NAV_GRAPH_PLAIN_SPEC_TEMPLATE
            .replace(
                oldValue = NavGraphCodeTemplates.PLACEHOLDER_NAV_GRAPH_SPEC_NAME,
                newValue = navGraphInfo.specImport.simpleName
            ).replace(
                oldValue = NavGraphCodeTemplates.PLACEHOLDER_NAV_GRAPH_SPEC_BASE_ROUTE,
                newValue = navGraphInfo.baseRoute
            ).replace(
                oldValue = NavGraphCodeTemplates.PLACEHOLDER_NAV_GRAPH_SPEC_PARENT_NAV_GRAPH_SPEC,
                newValue = navGraphInfo.parentNavGraphSpecImport?.simpleName ?: "null"
            ).replace(
                oldValue = NavGraphCodeTemplates.PLACEHOLDER_NAV_GRAPH_SPEC_START_COMPONENT,
                newValue = navGraphInfo.startComponentDeclaration.simpleName
            ).replace(
                oldValue = NavGraphCodeTemplates.PLACEHOLDER_NAV_GRAPH_SPEC_CHILD_NAV_COMPONENT_SPECS,
                newValue = generateChildNavComponentSpecParams(navGraphInfo)
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
                PackageUtils.NAV_COMPOSE_GRAPH_SPEC_IMPORT,
                PackageUtils.NAV_ARGUMENT_IMPORT,
                PackageUtils.ANDROID_NAVIGATION_NAMED_NAV_ARGUMENT_IMPORT,
                PackageUtils.ROUTABLE_IMPORT,
                PackageUtils.ANDROID_NAVIGATION_NAV_BACK_STACK_ENTRY_IMPORT,
            ).apply {
                addAll(argNavGraphSpec.allImports)
            },
            code = NavGraphCodeTemplates.NAV_GRAPH_ARG_SPEC_TEMPLATE
                .replace(
                    oldValue = NavGraphCodeTemplates.PLACEHOLDER_NAV_GRAPH_SPEC_NAME,
                    newValue = argNavGraphSpec.specImport.simpleName
                ).replace(
                    oldValue = NavGraphCodeTemplates.PLACEHOLDER_NAV_GRAPH_SPEC_BASE_ROUTE,
                    newValue = argNavGraphSpec.baseRoute
                ).replace(
                    oldValue = NavGraphCodeTemplates.PLACEHOLDER_NAV_GRAPH_SPEC_PARENT_NAV_GRAPH_SPEC,
                    newValue = argNavGraphSpec.parentNavGraphSpecImport?.simpleName ?: "null"
                ).replace(
                    oldValue = NavGraphCodeTemplates.PLACEHOLDER_NAV_GRAPH_SPEC_START_COMPONENT,
                    newValue = argNavGraphSpec.startComponentDeclaration.simpleName
                ).replace(
                    oldValue = NavGraphCodeTemplates.PLACEHOLDER_NAV_GRAPH_SPEC_CHILD_NAV_COMPONENT_SPECS,
                    newValue = generateChildNavComponentSpecParams(argNavGraphSpec)
                ).replace(
                    oldValue = NavGraphCodeTemplates.PLACEHOLDER_NAV_ARG_SPEC_NAV_ARG_TYPE,
                    newValue = argNavGraphSpec.navArgsInfo.name
                ).replace(
                    oldValue = NavGraphCodeTemplates.PLACEHOLDER_NAV_ARG_SPEC_ROUTE_ARGS,
                    newValue = NavArgsGeneratorUtils.generateRoute(sortedParams)
                ).replace(
                    oldValue = NavGraphCodeTemplates.PLACEHOLDER_NAV_ARG_SPEC_INVOKE_FUNCTION_PARAMETER,
                    newValue = NavArgsGeneratorUtils.generateInvokeParameters(argNavGraphSpec)
                ).replace(
                    oldValue = NavGraphCodeTemplates.PLACEHOLDER_NAV_ARG_SPEC_INVOKE_FUNCTION_BODY,
                    newValue = NavArgsGeneratorUtils.generateInvokeBody(argNavGraphSpec.baseRoute, sortedParams)
                ).replace(
                    oldValue = NavGraphCodeTemplates.PLACEHOLDER_NAV_ARG_SPEC_NAMED_ARGUMENTS,
                    newValue = NavArgsGeneratorUtils.generateNamedNavArguments(sortedParams)
                ).replace(
                    oldValue = NavGraphCodeTemplates.PLACEHOLDER_NAV_ARG_SPEC_GET_ARGS_BACKSTACK,
                    newValue = NavArgsGeneratorUtils.generateGetArgsBody(argNavGraphSpec, ArgContainer.NabBackStackEntry)
                ).replace(
                    oldValue = NavDestinationCodeTemplates.PLACEHOLDER_NAV_SPEC_DEEPLINK_VALUE,
                    newValue = "emptyList()"
                )
        )
    }

}