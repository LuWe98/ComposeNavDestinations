package com.welu.composenavdestinations.model.navargs

import com.welu.composenavdestinations.model.ImportInfo
import com.welu.composenavdestinations.utils.PackageUtils

enum class BasicParameterNavArgType(
    simpleName: String,
    override val importInfo: ImportInfo = ImportInfo(simpleName, PackageUtils.NAV_ARGS_PACKAGE)
): ParameterNavArgType {

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