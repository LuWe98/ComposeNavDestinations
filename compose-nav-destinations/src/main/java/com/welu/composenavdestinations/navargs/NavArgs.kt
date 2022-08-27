package com.welu.composenavdestinations.navargs

import android.os.Parcelable
import com.welu.composenavdestinations.extensions.*
import com.welu.composenavdestinations.extensions.marshall
import com.welu.composenavdestinations.extensions.receiveValueOf
import com.welu.composenavdestinations.util.Base64Util
import com.welu.composenavdestinations.util.ParseUtil
import java.io.Serializable
import kotlin.reflect.KClass

//String
object NavArgStringType: SingleNavArgType<String>(
    parseValueAction = { if(it == NavArgConstants.DECODED_EMPTY_STRING) "" else it },
    serializeValueAction = { it.ifEmpty { NavArgConstants.ENCODED_EMPTY_STRING } }
)

object NavArgStringListType: ListNavArgType<String>(NavArgStringType::parseValue, NavArgStringType::serializeValue)

object NavArgStringSetType: SetNavArgType<String>(NavArgStringType::parseValue, NavArgStringType::serializeValue)

object NavArgStringArrayType : ArrayNullableNavArgType<String>(
    clazz = String::class,
    emptyArrayProvider = ::emptyArray,
    arrayProvider = List<String?>::toTypedArray,
    parseValueAction = NavArgStringType::parseValue,
    serializeValueTransformation = NavArgStringType::serializeValue
)



//Char
object NavArgCharType: SingleNavArgType<Char>(ParseUtil::parseChar)

object NavArgCharListType: ListNavArgType<Char>(NavArgCharType::parseValue)

object NavArgCharSetType: SetNavArgType<Char>(NavArgCharType::parseValue)

object NavArgCharArrayType: ArrayNavArgType<CharArray, Char>(
    emptyArrayProvider = ::charArrayOf,
    arrayProvider = List<Char>::toCharArray,
    parseValueAction = ParseUtil::parseChar,
    arrayToStringTransformation = CharArray::joinToString
)



// Boolean
object NavArgBooleanType: SingleNavArgType<Boolean>(BoolType::parseValue)

object NavArgBooleanListType: ListNavArgType<Boolean>(NavArgBooleanType::parseValue)

object NavArgBooleanSetType: SetNavArgType<Boolean>(NavArgBooleanType::parseValue)

object NavArgBooleanArrayType: ArrayNavArgType<BooleanArray, Boolean>(
    emptyArrayProvider = ::booleanArrayOf,
    arrayProvider = List<Boolean>::toBooleanArray,
    parseValueAction = BoolType::parseValue,
    arrayToStringTransformation = BooleanArray::joinToString
)



// Byte
object NavArgByteType: SingleNavArgType<Byte>(ParseUtil::parseByte)

object NavArgByteListType: ListNavArgType<Byte>(NavArgByteType::parseValue)

object NavArgByteSetType: SetNavArgType<Byte>(NavArgByteType::parseValue)

object NavArgByteArrayType: ArrayNavArgType<ByteArray, Byte>(
    emptyArrayProvider = ::byteArrayOf,
    arrayProvider = List<Byte>::toByteArray,
    parseValueAction = ParseUtil::parseByte,
    arrayToStringTransformation = ByteArray::joinToString
)



// Short
object NavArgShortType: SingleNavArgType<Short>(ParseUtil::parseShort)

object NavArgShortListType: ListNavArgType<Short>(NavArgShortType::parseValue)

object NavArgShortSetType: SetNavArgType<Short>(NavArgShortType::parseValue)

object NavArgShortArrayType: ArrayNavArgType<ShortArray, Short>(
    emptyArrayProvider = ::shortArrayOf,
    arrayProvider = List<Short>::toShortArray,
    parseValueAction = ParseUtil::parseShort,
    arrayToStringTransformation = ShortArray::joinToString
)



// Int
object NavArgIntType: SingleNavArgType<Int>(IntType::parseValue)

object NavArgIntListType: ListNavArgType<Int>(NavArgIntType::parseValue)

object NavArgIntSetType: SetNavArgType<Int>(NavArgIntType::parseValue)

object NavArgIntArrayType: ArrayNavArgType<IntArray, Int>(
    emptyArrayProvider = ::intArrayOf,
    arrayProvider = List<Int>::toIntArray,
    parseValueAction = IntType::parseValue,
    arrayToStringTransformation = IntArray::joinToString
)



// Long
object NavArgLongType: SingleNavArgType<Long>(LongType::parseValue)

object NavArgLongListType: ListNavArgType<Long>(NavArgLongType::parseValue)

object NavArgLongSetType: SetNavArgType<Long>(NavArgLongType::parseValue)

object NavArgLongArrayType : ArrayNavArgType<LongArray, Long>(
    emptyArrayProvider = ::longArrayOf,
    arrayProvider = List<Long>::toLongArray,
    parseValueAction = LongType::parseValue,
    arrayToStringTransformation = LongArray::joinToString
)



// Float
object NavArgFloatType: SingleNavArgType<Float>(FloatType::parseValue)

object NavArgFloatListType: ListNavArgType<Float>(NavArgFloatType::parseValue)

object NavArgFloatSetType: SetNavArgType<Float>(NavArgFloatType::parseValue)

object NavArgFloatArrayType : ArrayNavArgType<FloatArray, Float>(
    emptyArrayProvider = ::floatArrayOf,
    arrayProvider = List<Float>::toFloatArray,
    parseValueAction = FloatType::parseValue,
    arrayToStringTransformation = FloatArray::joinToString
)



// Double
object NavArgDoubleType: SingleNavArgType<Double>(ParseUtil::parseDouble)

object NavArgDoubleListType: ListNavArgType<Double>(NavArgDoubleType::parseValue)

object NavArgDoubleSetType: SetNavArgType<Double>(NavArgDoubleType::parseValue)

object NavArgDoubleArrayType : ArrayNavArgType<DoubleArray, Double>(
    emptyArrayProvider = ::doubleArrayOf,
    arrayProvider = List<Double>::toDoubleArray,
    parseValueAction = ParseUtil::parseDouble,
    arrayToStringTransformation = DoubleArray::joinToString
)



// Enum
class NavArgEnumType<T: Enum<T>>(enumClass: KClass<T>): SingleNavArgType<T>(enumClass::receiveValueOf, Enum<T>::name)

class NavArgEnumListType<T: Enum<T>>(enumClass: KClass<T>): ListNavArgType<T>(enumClass::receiveValueOf, Enum<T>::name)

class NavArgEnumSetType<T: Enum<T>>(enumClass: KClass<T>): SetNavArgType<T>(enumClass::receiveValueOf, Enum<T>::name)

class NavArgEnumArrayType<T: Enum<T>>(enumClass: KClass<T>): ArrayNullableNavArgType<T>(enumClass, enumClass::receiveValueOf, Enum<T>::name)



// Parcelable
//TODO -> Creator beim erstellen der Klasse holen, damit man den nicht öfters immer wieder holen muss
class NavArgParcelableType<T : Parcelable>(creator: Parcelable.Creator<T>): SingleNavArgType<T>(creator::unmarshall, Parcelable::marshall) {
    constructor(clazz: KClass<T>): this(clazz.CREATOR)
}

class NavArgParcelableListType<T : Parcelable>(creator: Parcelable.Creator<T>): ListNavArgType<T>(creator::unmarshall, Parcelable::marshall) {
    constructor(clazz: KClass<T>): this(clazz.CREATOR)
}

class NavArgParcelableSetType<T : Parcelable>(creator: Parcelable.Creator<T>): SetNavArgType<T>(creator::unmarshall, Parcelable::marshall) {
    constructor(clazz: KClass<T>): this(clazz.CREATOR)
}

class NavArgParcelableArrayType<T : Parcelable>(clazz: KClass<T>, creator: Parcelable.Creator<T> = clazz.CREATOR): ArrayNullableNavArgType<T>(
    clazz = clazz,
    parseValueAction = creator::unmarshall,
    serializeValueTransformation = Parcelable::marshall
)



// Serializable
object NavArgSerializableType: SingleNavArgType<Serializable>(Base64Util::deserialize, Serializable::serialize)