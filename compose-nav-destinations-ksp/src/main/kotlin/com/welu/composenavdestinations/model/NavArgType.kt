package com.welu.composenavdestinations.model

import com.welu.composenavdestinations.utils.PackageUtils

sealed interface NavArgType {
    val simpleName: String
    val importInfo: ImportInfo
}

enum class BasicNavArgType(
    override val simpleName: String,
    override val importInfo: ImportInfo = ImportInfo(simpleName, PackageUtils.NAV_ARGS_PACKAGE)
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
    DoubleSetType("NavArgDoubleSetType");
}

enum class CustomNavArgType(
    override val simpleName: String,
    override val importInfo: ImportInfo = ImportInfo(simpleName, PackageUtils.NAV_ARGS_PACKAGE)
): NavArgType {
    ParcelableType("NavArgParcelableType"),
    ParcelableArrayType("NavArgParcelableArrayType"),
    ParcelableListType("NavArgParcelableListType"),
    ParcelableSetType("NavArgParcelableSetType"),

    EnumType("NavArgEnumType"),
    EnumArrayType("NavArgEnumArrayType"),
    EnumListType("NavArgEnumListType"),
    EnumSetType("NavArgEnumSetType"),

    SerializableType("NavArgSerializableType");
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