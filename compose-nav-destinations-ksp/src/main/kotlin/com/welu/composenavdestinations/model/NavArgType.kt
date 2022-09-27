package com.welu.composenavdestinations.model

import com.welu.composenavdestinations.utils.PackageUtils

sealed interface NavArgType {
    val simpleName: String
    val importInfo: ImportInfo get() = ImportInfo(simpleName, PackageUtils.NAV_ARGS_PACKAGE)
}

enum class BasicNavArgType(
    override val simpleName: String
): NavArgType {

    StringType("NavArgStringType"),
    StringArrayType("NavArgStringArrayType"),
    StringListType("NavArgStringListType"),
    StringSetType("NavArgStringSetType"),

    CharType("NavArgCharType"),
    PrimitiveCharArrayType("NavArgPrimitiveCharArrayType"),
    CharArrayType("NavArgCharArrayType"),
    CharListType("NavArgCharListType"),
    CharSetType("NavArgCharSetType"),

    LongType("NavArgLongType"),
    PrimitiveLongArrayType("NavArgPrimitiveLongArrayType"),
    LongArrayType("NavArgLongArrayType"),
    LongListType("NavArgLongListType"),
    LongSetType("NavArgLongSetType"),

    IntType("NavArgIntType"),
    PrimitiveIntArrayType("NavArgPrimitiveIntArrayType"),
    IntArrayType("NavArgIntArrayType"),
    IntListType("NavArgIntListType"),
    IntSetType("NavArgIntSetType"),

    ShortType("NavArgShortType"),
    PrimitiveShortArrayType("NavArgPrimitiveShortArrayType"),
    ShortArrayType("NavArgShortArrayType"),
    ShortListType("NavArgShortListType"),
    ShortSetType("NavArgShortSetType"),

    ByteType("NavArgByteType"),
    PrimitiveByteArrayType("NavArgPrimitiveByteArrayType"),
    ByteArrayType("NavArgByteArrayType"),
    ByteListType("NavArgByteListType"),
    ByteSetType("NavArgByteSetType"),

    BooleanType("NavArgBooleanType"),
    PrimitiveBooleanArrayType("NavArgPrimitiveBooleanArrayType"),
    BooleanArrayType("NavArgBooleanArrayType"),
    BooleanListType("NavArgBooleanListType"),
    BooleanSetType("NavArgBooleanSetType"),

    FloatType("NavArgFloatType"),
    PrimitiveFloatArrayType("NavArgPrimitiveFloatArrayType"),
    FloatArrayType("NavArgFloatArrayType"),
    FloatListType("NavArgFloatListType"),
    FloatSetType("NavArgFloatSetType"),

    DoubleType("NavArgDoubleType"),
    PrimitiveDoubleArrayType("NavArgPrimitiveDoubleArrayType"),
    DoubleArrayType("NavArgDoubleArrayType"),
    DoubleListType("NavArgDoubleListType"),
    DoubleSetType("NavArgDoubleSetType"),

    SerializableType("NavArgSerializableType");
}

//enum class CustomNavArgType(
//    override val simpleName: String
//): NavArgType {
//    ParcelableType("NavArgParcelableType"),
//    ParcelableArrayType("NavArgParcelableArrayType"),
//    ParcelableListType("NavArgParcelableListType"),
//    ParcelableSetType("NavArgParcelableSetType"),
//
//    EnumType("NavArgEnumType"),
//    EnumArrayType("NavArgEnumArrayType"),
//    EnumListType("NavArgEnumListType"),
//    EnumSetType("NavArgEnumSetType");
//}

sealed class CustomNavArgType(
    val generatedNavArgImport: ImportInfo,
    val parameterTypeImport: ImportInfo,
    override val simpleName: String
): NavArgType {

    class ParcelableType(
        generatedNavArgImport: ImportInfo,
        parameterTypeImport: ImportInfo
    ): CustomNavArgType(
        generatedNavArgImport = generatedNavArgImport,
        parameterTypeImport = parameterTypeImport,
        simpleName = "NavArgParcelableType"
    )

    class ParcelableArrayType(
        generatedNavArgImport: ImportInfo,
        parameterTypeImport: ImportInfo
    ): CustomNavArgType(
        generatedNavArgImport = generatedNavArgImport,
        parameterTypeImport = parameterTypeImport,
        simpleName = "NavArgParcelableArrayType"
    )

    class ParcelableListType(
        generatedNavArgImport: ImportInfo,
        parameterTypeImport: ImportInfo
    ): CustomNavArgType(
        generatedNavArgImport = generatedNavArgImport,
        parameterTypeImport = parameterTypeImport,
        simpleName = "NavArgParcelableListType"
    )

    class ParcelableSetType(
        generatedNavArgImport: ImportInfo,
        parameterTypeImport: ImportInfo
    ): CustomNavArgType(
        generatedNavArgImport = generatedNavArgImport,
        parameterTypeImport = parameterTypeImport,
        simpleName = "NavArgParcelableSetType"
    )

    class EnumType(
        generatedNavArgImport: ImportInfo,
        parameterTypeImport: ImportInfo
    ): CustomNavArgType(
        generatedNavArgImport = generatedNavArgImport,
        parameterTypeImport = parameterTypeImport,
        simpleName = "NavArgEnumType"
    )

    class EnumArrayType(
        generatedNavArgImport: ImportInfo,
        parameterTypeImport: ImportInfo
    ): CustomNavArgType(
        generatedNavArgImport = generatedNavArgImport,
        parameterTypeImport = parameterTypeImport,
        simpleName = "NavArgEnumArrayType"
    )

    class EnumListType(
        generatedNavArgImport: ImportInfo,
        parameterTypeImport: ImportInfo
    ): CustomNavArgType(
        generatedNavArgImport = generatedNavArgImport,
        parameterTypeImport = parameterTypeImport,
        simpleName = "NavArgEnumListType"
    )

    class EnumSetType(
        generatedNavArgImport: ImportInfo,
        parameterTypeImport: ImportInfo
    ): CustomNavArgType(
        generatedNavArgImport = generatedNavArgImport,
        parameterTypeImport = parameterTypeImport,
        simpleName = "NavArgEnumSetType"
    )
}



//enum class NavArgType(
//    val simpleName: String,
//    val importInfo: ImportInfo = ImportInfo(simpleName, PackageUtils.NAV_ARGS_PACKAGE)
//) {
//    StringType("NavArgStringType"),
//    StringArrayType("NavArgStringArrayType"),
//    StringListType("NavArgStringListType"),
//    StringSetType("NavArgStringSetType"),
//
//    CharType("NavArgCharType"),
//    PrimitiveCharArrayType("NavArgPrimitiveCharArrayType"),
//    CharArrayType("NavArgCharArrayType"),
//    CharListType("NavArgCharListType"),
//    CharSetType("NavArgCharSetType"),
//
//    LongType("NavArgLongType"),
//    PrimitiveLongArrayType("NavArgPrimitiveLongArrayType"),
//    LongArrayType("NavArgLongArrayType"),
//    LongListType("NavArgLongListType"),
//    LongSetType("NavArgLongSetType"),
//
//    IntType("NavArgIntType"),
//    PrimitiveIntArrayType("NavArgPrimitiveIntArrayType"),
//    IntArrayType("NavArgIntArrayType"),
//    IntListType("NavArgIntListType"),
//    IntSetType("NavArgIntSetType"),
//
//    ShortType("NavArgShortType"),
//    PrimitiveShortArrayType("NavArgPrimitiveShortArrayType"),
//    ShortArrayType("NavArgShortArrayType"),
//    ShortListType("NavArgShortListType"),
//    ShortSetType("NavArgShortSetType"),
//
//    ByteType("NavArgByteType"),
//    PrimitiveByteArrayType("NavArgPrimitiveByteArrayType"),
//    ByteArrayType("NavArgByteArrayType"),
//    ByteListType("NavArgByteListType"),
//    ByteSetType("NavArgByteSetType"),
//
//    BooleanType("NavArgBooleanType"),
//    PrimitiveBooleanArrayType("NavArgPrimitiveBooleanArrayType"),
//    BooleanArrayType("NavArgBooleanArrayType"),
//    BooleanListType("NavArgBooleanListType"),
//    BooleanSetType("NavArgBooleanSetType"),
//
//    FloatType("NavArgFloatType"),
//    PrimitiveFloatArrayType("NavArgPrimitiveFloatArrayType"),
//    FloatArrayType("NavArgFloatArrayType"),
//    FloatListType("NavArgFloatListType"),
//    FloatSetType("NavArgFloatSetType"),
//
//    DoubleType("NavArgDoubleType"),
//    PrimitiveDoubleArrayType("NavArgPrimitiveDoubleArrayType"),
//    DoubleArrayType("NavArgDoubleArrayType"),
//    DoubleListType("NavArgDoubleListType"),
//    DoubleSetType("NavArgDoubleSetType"),
//
//    ParcelableType("NavArgParcelableType"),
//    ParcelableArrayType("NavArgParcelableArrayType"),
//    ParcelableListType("NavArgParcelableListType"),
//    ParcelableSetType("NavArgParcelableSetType"),
//
//    EnumType("NavArgEnumType"),
//    EnumArrayType("NavArgEnumArrayType"),
//    EnumListType("NavArgEnumListType"),
//    EnumSetType("NavArgEnumSetType"),
//
//    SerializableType("NavArgSerializableType");
//}