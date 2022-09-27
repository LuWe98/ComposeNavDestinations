package com.welu.composenavdestinations.model.navargs

import com.welu.composenavdestinations.model.ImportInfo
import com.welu.composenavdestinations.utils.PackageUtils

sealed class ComplexParameterNavArgType(
    val generatedNavArgImport: ImportInfo,
    val parameterTypeImport: ImportInfo,
    simpleName: String,
    override val importInfo: ImportInfo = ImportInfo(simpleName, PackageUtils.NAV_ARGS_PACKAGE)
): ParameterNavArgType {

    class ParcelableType(
        generatedNavArgImport: ImportInfo,
        parameterTypeImport: ImportInfo
    ): ComplexParameterNavArgType(
        generatedNavArgImport = generatedNavArgImport,
        parameterTypeImport = parameterTypeImport,
        simpleName = "NavArgParcelableType"
    )

    class ParcelableArrayType(
        generatedNavArgImport: ImportInfo,
        parameterTypeImport: ImportInfo
    ): ComplexParameterNavArgType(
        generatedNavArgImport = generatedNavArgImport,
        parameterTypeImport = parameterTypeImport,
        simpleName = "NavArgParcelableArrayType"
    )

    class ParcelableListType(
        generatedNavArgImport: ImportInfo,
        parameterTypeImport: ImportInfo
    ): ComplexParameterNavArgType(
        generatedNavArgImport = generatedNavArgImport,
        parameterTypeImport = parameterTypeImport,
        simpleName = "NavArgParcelableListType"
    )

    class ParcelableSetType(
        generatedNavArgImport: ImportInfo,
        parameterTypeImport: ImportInfo
    ): ComplexParameterNavArgType(
        generatedNavArgImport = generatedNavArgImport,
        parameterTypeImport = parameterTypeImport,
        simpleName = "NavArgParcelableSetType"
    )

    class EnumType(
        generatedNavArgImport: ImportInfo,
        parameterTypeImport: ImportInfo
    ): ComplexParameterNavArgType(
        generatedNavArgImport = generatedNavArgImport,
        parameterTypeImport = parameterTypeImport,
        simpleName = "NavArgEnumType"
    )

    class EnumArrayType(
        generatedNavArgImport: ImportInfo,
        parameterTypeImport: ImportInfo
    ): ComplexParameterNavArgType(
        generatedNavArgImport = generatedNavArgImport,
        parameterTypeImport = parameterTypeImport,
        simpleName = "NavArgEnumArrayType"
    )

    class EnumListType(
        generatedNavArgImport: ImportInfo,
        parameterTypeImport: ImportInfo
    ): ComplexParameterNavArgType(
        generatedNavArgImport = generatedNavArgImport,
        parameterTypeImport = parameterTypeImport,
        simpleName = "NavArgEnumListType"
    )

    class EnumSetType(
        generatedNavArgImport: ImportInfo,
        parameterTypeImport: ImportInfo
    ): ComplexParameterNavArgType(
        generatedNavArgImport = generatedNavArgImport,
        parameterTypeImport = parameterTypeImport,
        simpleName = "NavArgEnumSetType"
    )
}