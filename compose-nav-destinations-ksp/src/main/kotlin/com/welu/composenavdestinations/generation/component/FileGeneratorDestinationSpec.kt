package com.welu.composenavdestinations.generation.component

import com.welu.composenavdestinations.extensions.div
import com.welu.composenavdestinations.generation.FileContentInfoTypedGenerator
import com.welu.composenavdestinations.generation.templates.NavDestinationCodeTemplates
import com.welu.composenavdestinations.model.AndroidArgsContainer
import com.welu.composenavdestinations.model.FileContentInfo
import com.welu.composenavdestinations.model.Parameter
import com.welu.composenavdestinations.model.ParameterTypeInfo
import com.welu.composenavdestinations.model.components.ComposeDestinationInfo
import com.welu.composenavdestinations.utils.PackageUtils

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
            PackageUtils.ANDROID_NAVIGATION_DEEP_LINK_IMPORT
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
                PackageUtils.ANDROID_NAVIGATION_DEEP_LINK_IMPORT,
                PackageUtils.ANDROID_NAVIGATION_NAV_BACK_STACK_ENTRY_IMPORT,
                PackageUtils.NAV_ARGUMENT_IMPORT,
                PackageUtils.ANDROID_NAVIGATION_NAMED_NAV_ARGUMENT_IMPORT,
                PackageUtils.SAVED_STATE_HANDLE_IMPORT,
                PackageUtils.ROUTABLE_IMPORT
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
                    newValue = NavArgsGeneratorUtils.generateGetArgsBody(argDestinationInfo, AndroidArgsContainer.NabBackStackEntry)
                ).replace(
                    oldValue = NavDestinationCodeTemplates.PLACEHOLDER_NAV_ARG_SPEC_GET_ARGS_SAVED_STATE,
                    newValue = NavArgsGeneratorUtils.generateGetArgsBody(argDestinationInfo, AndroidArgsContainer.SaveStateHandle)
                ).replace(
                    oldValue = NavDestinationCodeTemplates.PLACEHOLDER_DESTINATION_TYPE_SPEC_NAME,
                    newValue = argDestinationInfo.destinationType.specImportInfo.simpleName
                ).replace(
                    oldValue = NavDestinationCodeTemplates.PLACEHOLDER_NAV_SPEC_DEEPLINK_VALUE,
                    newValue = "emptyList()"
                )
        )
    }


//    private fun generateRoute(sortedParams: List<Parameter>): String {
//        var optionalNavSeparator = '?'
//        return sortedParams.joinToString(" +\n\t\t") {
//            if (it.typeInfo.isNullable || it.hasDefaultValue) {
//                "\"$optionalNavSeparator${it.name}={${it.name}}\"".also {
//                    optionalNavSeparator = '&'
//                }
//            } else {
//                "\"/{${it.name}}\""
//            }
//        }
//    }
//
//    private fun generateNamedNavArguments(sortedParams: List<Parameter>) = sortedParams.joinToString(",\n\t\t") { parameter ->
//        val defaultValue = parameter.defaultValue?.let { ", ${it.value}" } ?: ""
//        "navArgument(\"${parameter.name}\", ${parameter.navArgTypeInfo.simpleName}$defaultValue,${parameter.typeInfo.isNullable})"
//    }
//
//    private fun generateInvokeParameters(component: NavComponentInfo) = component
//        .navArgsInfo!!
//        .parameters
//        .joinToString(",\n\t\t", transform = Parameter::fullDeclarationName)
//
//    private fun generateInvokeBody(routeName: String, sortedParams: List<Parameter>): String {
//        var optionalNavSeparator = '?'
//        return "\"$routeName\" +\n\t\t" + sortedParams.joinToString(" +\n\t\t") {
//            val serializeSnipped = "\${${it.navArgTypeInfo.simpleName}.serializeValue(${it.name})}"
//            if (it.typeInfo.isNullable || it.hasDefaultValue) {
//                "\"$optionalNavSeparator${it.name}=$serializeSnipped\"".also {
//                    optionalNavSeparator = '&'
//                }
//            } else {
//                "\"/$serializeSnipped\""
//            }
//        }
//    }
//
//    private fun generateGetArgsBody(component: NavComponentInfo, argContainer: ArgContainer): String = component
//        .navArgsInfo!!
//        .parameters
//        .joinToString(",\n\t\t") {
//        val nonNullableClaim = if (it.typeInfo.isNullable) "" else "!!"
//        it.name + " = " + "${it.navArgTypeInfo.simpleName}.getTyped(${argContainer.variableName}, \"${it.name}\")$nonNullableClaim"
//    }

}


//    //.replace(CodeTemplates.PLACEHOLDER_NAV_ARG_SPEC_GENERATED_NAV_ARG, generateCustomNavArgClass(instance))
//    private fun generateCustomNavArgClass(instance: NavDestinationInfo): String {
//        if (instance.navArgsInfo != null) return ""
//        val args = instance.navArgsInfo?.parameters?.joinToString(",\n\t\t") {
//            "val " + it.fullDeclarationName
//        }
//        return "data class NavArgs(\n\t\t$args\n\t)".trimIndent()
//    }
