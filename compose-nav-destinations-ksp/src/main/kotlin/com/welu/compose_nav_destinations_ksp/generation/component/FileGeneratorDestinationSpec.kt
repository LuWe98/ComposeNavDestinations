package com.welu.compose_nav_destinations_ksp.generation.component

import com.welu.compose_nav_destinations_ksp.extensions.div
import com.welu.compose_nav_destinations_ksp.generation.FileContentInfoTypedGenerator
import com.welu.compose_nav_destinations_ksp.generation.templates.NavDestinationCodeTemplates
import com.welu.compose_nav_destinations_ksp.model.ArgumentContainer
import com.welu.compose_nav_destinations_ksp.model.FileContentInfo
import com.welu.compose_nav_destinations_ksp.model.Parameter
import com.welu.compose_nav_destinations_ksp.model.ParameterTypeInfo
import com.welu.compose_nav_destinations_ksp.model.components.ComposeDestinationInfo
import com.welu.compose_nav_destinations_ksp.utils.ImportUtils

object FileGeneratorDestinationSpec : FileContentInfoTypedGenerator<ComposeDestinationInfo> {

    override fun generate(instance: ComposeDestinationInfo): FileContentInfo = if (instance.isArgDestination) {
        generateArgSpecFileContentInfo(instance)
    } else {
        generatePlainSpecFileContentInfo(instance)
    }

    private fun generatePlainSpecFileContentInfo(plainDestinationInfo: ComposeDestinationInfo): FileContentInfo = FileContentInfo(
        fileName = plainDestinationInfo.simpleName,
        packageDir = plainDestinationInfo.packageDir,
        imports = setOf(
            plainDestinationInfo.destinationImport,
            plainDestinationInfo.parentNavGraphSpecImport,
            plainDestinationInfo.destinationType.specImportInfo,
            ImportUtils.ANDROID_NAVIGATION_DEEP_LINK_IMPORT
        ),
        code = NavDestinationCodeTemplates.NAV_DESTINATION_PLAIN_SPEC_TEMPLATE
            .replace(
                oldValue = NavDestinationCodeTemplates.PLACEHOLDER_NAV_SPEC_BASE_ROUTE,
                newValue = plainDestinationInfo.baseRoute
            ).replace(
                oldValue = NavDestinationCodeTemplates.PLACEHOLDER_NAV_SPEC_DESTINATION_NAME,
                newValue = plainDestinationInfo.simpleName
            ).replace(
                oldValue = NavDestinationCodeTemplates.PLACEHOLDER_NAV_DESTINATION_NAME,
                newValue = plainDestinationInfo.destinationImport.simpleName
            ).replace(
                oldValue = NavDestinationCodeTemplates.PLACEHOLDER_NAV_DESTINATION_NAV_GRAPH,
                newValue = plainDestinationInfo.parentNavGraphSpecImport.simpleName
            ).replace(
                oldValue = NavDestinationCodeTemplates.PLACEHOLDER_DESTINATION_TYPE_SPEC_NAME,
                newValue = plainDestinationInfo.destinationType.specImportInfo.simpleName
            ).replace(
                oldValue = NavDestinationCodeTemplates.PLACEHOLDER_NAV_SPEC_DEEPLINK_VALUE,
                newValue = "emptyList()"
            )
    )

    private fun generateArgSpecFileContentInfo(argDestinationInfo: ComposeDestinationInfo): FileContentInfo {
        val sortedParams = argDestinationInfo
            .navArgsInfo!!
            .parameters
            .sortedWith(compareBy(Parameter::typeInfo / ParameterTypeInfo::isNullable, Parameter::hasDefaultValue))

        return FileContentInfo(
            fileName = argDestinationInfo.simpleName,
            packageDir = argDestinationInfo.packageDir,
            imports = mutableSetOf(
                argDestinationInfo.parentNavGraphSpecImport,
                argDestinationInfo.destinationType.specImportInfo,
                ImportUtils.ANDROID_NAVIGATION_DEEP_LINK_IMPORT,
                ImportUtils.ANDROID_NAVIGATION_NAV_BACK_STACK_ENTRY_IMPORT,
                ImportUtils.NAV_ARGUMENT_IMPORT,
                ImportUtils.ANDROID_NAVIGATION_NAMED_NAV_ARGUMENT_IMPORT,
                ImportUtils.SAVED_STATE_HANDLE_IMPORT,
                ImportUtils.ROUTABLE_IMPORT
            ).apply {
                addAll(argDestinationInfo.imports)
            },
            code = NavDestinationCodeTemplates.NAV_DESTINATION_ARG_SPEC_TEMPLATE
                .replace(
                    oldValue = NavDestinationCodeTemplates.PLACEHOLDER_NAV_SPEC_BASE_ROUTE,
                    newValue = argDestinationInfo.baseRoute
                ).replace(
                    oldValue = NavDestinationCodeTemplates.PLACEHOLDER_NAV_DESTINATION_NAME,
                    newValue = argDestinationInfo.destinationImport.simpleName
                ).replace(
                    oldValue = NavDestinationCodeTemplates.PLACEHOLDER_NAV_DESTINATION_NAV_GRAPH,
                    newValue = argDestinationInfo.parentNavGraphSpecImport.simpleName
                ).replace(
                    oldValue = NavDestinationCodeTemplates.PLACEHOLDER_NAV_SPEC_DESTINATION_NAME,
                    newValue = argDestinationInfo.simpleName
                ).replace(
                    oldValue = NavDestinationCodeTemplates.PLACEHOLDER_NAV_ARG_SPEC_NAV_ARG_TYPE,
                    newValue = argDestinationInfo.navArgsInfo.name
                ).replace(
                    oldValue = NavDestinationCodeTemplates.PLACEHOLDER_NAV_ARG_SPEC_ROUTE_ARGS,
                    newValue = NavArgsGeneratorUtils.generateRoute(sortedParams)
                ).replace(
                    oldValue = NavDestinationCodeTemplates.PLACEHOLDER_NAV_ARG_SPEC_INVOKE_FUNCTION_PARAMETER,
                    newValue = NavArgsGeneratorUtils.generateInvokeParameters(argDestinationInfo)
                ).replace(
                    oldValue = NavDestinationCodeTemplates.PLACEHOLDER_NAV_ARG_SPEC_INVOKE_FUNCTION_BODY,
                    newValue = NavArgsGeneratorUtils.generateInvokeBody(argDestinationInfo.baseRoute, sortedParams)
                ).replace(
                    oldValue = NavDestinationCodeTemplates.PLACEHOLDER_NAV_ARG_SPEC_NAMED_ARGUMENTS,
                    newValue = NavArgsGeneratorUtils.generateNamedNavArguments(sortedParams)
                ).replace(
                    oldValue = NavDestinationCodeTemplates.PLACEHOLDER_NAV_ARG_SPEC_GET_ARGS_BACKSTACK,
                    newValue = NavArgsGeneratorUtils.generateGetArgsBody(argDestinationInfo, ArgumentContainer.NabBackStackEntry)
                ).replace(
                    oldValue = NavDestinationCodeTemplates.PLACEHOLDER_NAV_ARG_SPEC_GET_ARGS_SAVED_STATE,
                    newValue = NavArgsGeneratorUtils.generateGetArgsBody(argDestinationInfo, ArgumentContainer.SaveStateHandle)
                ).replace(
                    oldValue = NavDestinationCodeTemplates.PLACEHOLDER_DESTINATION_TYPE_SPEC_NAME,
                    newValue = argDestinationInfo.destinationType.specImportInfo.simpleName
                ).replace(
                    oldValue = NavDestinationCodeTemplates.PLACEHOLDER_NAV_SPEC_DEEPLINK_VALUE,
                    newValue = "emptyList()"
                )
        )
    }
}