package com.welu.composenavdestinations.model

import com.welu.composenavdestinations.utils.PackageUtils

data class ParameterNavTypeInfo(
    //TODO -> Was das für ein Import? Nur von dem NavType? -> Wenn ja dann kann hier NavArgType SealedClass rein
    val import: ImportInfo,
    val customNavTypeInfo: ParameterCustomNavTypeInfo? = null
) {

    constructor(typeInfo: ParameterTypeInfo, customNavArgType: CustomNavArgType): this(
        import = ImportInfo(typeInfo.qualifiedName.replace(".", "_") + "_" + customNavArgType.simpleName, PackageUtils.NAV_ARGS_PACKAGE),
        customNavTypeInfo = ParameterCustomNavTypeInfo(
            parameterTypeImport = typeInfo.type.import,
            navArgType = customNavArgType
        )
    )

    val simpleName: String get() = import.simpleName

}