package com.welu.composenavdestinations.model

import com.welu.composenavdestinations.utils.PackageUtils

data class ParameterNavArgInfo(
    val import: ImportInfo,
    val customNavArgInfo: CustomParameterNavArgInfo? = null
) {
    constructor(typeInfo: ParameterTypeInfo, customNavArgTypeImportInfo: ImportInfo): this(
        import = ImportInfo(typeInfo.qualifiedName.replace(".", "_") + "_" + customNavArgTypeImportInfo.simpleName, PackageUtils.NAV_ARGS_PACKAGE),
        customNavArgInfo = CustomParameterNavArgInfo(typeInfo.type.import, customNavArgTypeImportInfo)
    )
}