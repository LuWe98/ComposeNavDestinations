package com.welu.composenavdestinations.model

import com.welu.composenavdestinations.utils.PackageUtils

data class ParameterNavTypeInfo(
    val import: ImportInfo,
    val customNavTypeInfo: ParameterCustomNavTypeInfo? = null
) {

    val simpleName: String get() = import.simpleName

    constructor(typeInfo: ParameterTypeInfo, customNavArgTypeImportInfo: ImportInfo): this(
        import = ImportInfo(typeInfo.qualifiedName.replace(".", "_") + "_" + customNavArgTypeImportInfo.simpleName, PackageUtils.NAV_ARGS_PACKAGE),
        customNavTypeInfo = ParameterCustomNavTypeInfo(
            parameterTypeImport = typeInfo.type.import,
            navArgTypeImport = customNavArgTypeImportInfo
        )
    )
}