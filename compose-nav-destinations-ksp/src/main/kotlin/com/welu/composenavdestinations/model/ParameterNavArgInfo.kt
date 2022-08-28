package com.welu.composenavdestinations.model

data class ParameterNavArgInfo(
    val import: ImportInfo,
    val customNavArgType: CustomNavArgType? = null
) {
    constructor(typeInfo: ParameterTypeInfo, customNavArgType: CustomNavArgType): this(
        import = ImportInfo(typeInfo.asCustomNavArgName + customNavArgType.suffix),
        customNavArgType = customNavArgType
    )
}