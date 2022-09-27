package com.welu.composenavdestinations.model

import com.welu.composenavdestinations.utils.PackageUtils

data class ParameterNavTypeInfo(
    val navArgType: NavArgType,
    val customNavTypeInfo: ParameterCustomNavTypeInfo? = null
) {

    constructor(typeInfo: ParameterTypeInfo, customNavArgType: CustomNavArgType): this(
        navArgType = customNavArgType,
        customNavTypeInfo = ParameterCustomNavTypeInfo(
            parameterTypeImport = typeInfo.type.import,
            generatedCustomNavArgTypeImport = ImportInfo(
                simpleName = typeInfo.qualifiedName.replace(".", "_") + "_" + customNavArgType.simpleName,
                packageDir = PackageUtils.NAV_ARGS_PACKAGE
            )
        )
    )
    //ImportInfo(typeInfo.qualifiedName.replace(".", "_") + "_" + customNavArgType.simpleName, PackageUtils.NAV_ARGS_PACKAGE)

    val import: ImportInfo get() = customNavTypeInfo?.generatedCustomNavArgTypeImport ?: navArgType.importInfo

    val simpleName: String get() = navArgType.simpleName

}