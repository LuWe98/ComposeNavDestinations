package com.welu.compose_nav_destinations_ksp.model.navargs

import com.welu.compose_nav_destinations_ksp.model.ImportInfo
import com.welu.compose_nav_destinations_ksp.utils.ImportUtils

sealed class ComplexParameterNavArgType(
    val generatedNavArgImport: ImportInfo,
    val parameterTypeImport: ImportInfo,
    simpleName: String
): ParameterNavArgType {

    override val importInfo: ImportInfo = ImportInfo(simpleName, ImportUtils.NAV_ARGS_PACKAGE)

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

    class KotlinSerializableType(
        generatedNavArgImport: ImportInfo,
        parameterTypeImport: ImportInfo
    ): ComplexParameterNavArgType(
        generatedNavArgImport = generatedNavArgImport,
        parameterTypeImport = parameterTypeImport,
        simpleName = "NavArgKotlinSerializableType"
    )
}