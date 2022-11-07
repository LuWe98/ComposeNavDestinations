package com.welu.composenavdestinations.navargs

import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import com.welu.composenavdestinations.extensions.*
import com.welu.composenavdestinations.extensions.marshall
import com.welu.composenavdestinations.extensions.findValueOf
import com.welu.composenavdestinations.extensions.navigation.get
import com.welu.composenavdestinations.extensions.serialize
import com.welu.composenavdestinations.util.*
import com.welu.composenavdestinations.util.Base64Util
import com.welu.composenavdestinations.util.deserialize
import com.welu.composenavdestinations.util.ktxSerializer
import kotlinx.serialization.KSerializer
import java.io.Serializable
import kotlin.reflect.KClass

//String
object NavArgStringType : SingleNavArgType<String>(
    parseValueAction = { if (it == NavArgConstants.DECODED_EMPTY_STRING) "" else it },
    serializeValueAction = { it.ifEmpty { NavArgConstants.ENCODED_EMPTY_STRING } }
)

object NavArgStringListType : ListNavArgType<String>(NavArgStringType::parseValue, NavArgStringType::serializeValue)

object NavArgStringSetType : SetNavArgType<String>(NavArgStringType::parseValue, NavArgStringType::serializeValue)

object NavArgStringArrayType : ArrayNavArgType<String>(
    emptyArrayProvider = ::emptyArray,
    arrayProvider = List<String?>::toTypedArray,
    parseValueAction = NavArgStringType::parseValue,
    serializeValueTransformation = NavArgStringType::serializeValue
)


//Char
object NavArgCharType : SingleNavArgType<Char>(ParseUtil::parseChar)

object NavArgCharListType : ListNavArgType<Char>(NavArgCharType::parseValue)

object NavArgCharSetType : SetNavArgType<Char>(NavArgCharType::parseValue)

object NavArgCharArrayType : ArrayNavArgType<Char>(
    emptyArrayProvider = ::emptyArray,
    arrayProvider = List<Char?>::toTypedArray,
    parseValueAction = NavArgCharType::parseValue
)

object NavArgPrimitiveCharArrayType : PrimitiveArrayNavArgType<CharArray, Char>(
    emptyArrayProvider = ::charArrayOf,
    arrayProvider = List<Char>::toCharArray,
    parseValueAction = ParseUtil::parseChar,
    arrayToStringTransformation = CharArray::joinToString
)


// Boolean
object NavArgBooleanType : SingleNavArgType<Boolean>(BoolType::parseValue)

object NavArgBooleanListType : ListNavArgType<Boolean>(NavArgBooleanType::parseValue)

object NavArgBooleanSetType : SetNavArgType<Boolean>(NavArgBooleanType::parseValue)

object NavArgBooleanArrayType : ArrayNavArgType<Boolean>(
    emptyArrayProvider = ::emptyArray,
    arrayProvider = List<Boolean?>::toTypedArray,
    parseValueAction = NavArgBooleanType::parseValue
)

object NavArgPrimitiveBooleanArrayType : PrimitiveArrayNavArgType<BooleanArray, Boolean>(
    emptyArrayProvider = ::booleanArrayOf,
    arrayProvider = List<Boolean>::toBooleanArray,
    parseValueAction = BoolType::parseValue,
    arrayToStringTransformation = BooleanArray::joinToString
)


// Byte
object NavArgByteType : SingleNavArgType<Byte>(ParseUtil::parseByte)

object NavArgByteListType : ListNavArgType<Byte>(NavArgByteType::parseValue)

object NavArgByteSetType : SetNavArgType<Byte>(NavArgByteType::parseValue)

object NavArgByteArrayType : ArrayNavArgType<Byte>(
    emptyArrayProvider = ::emptyArray,
    arrayProvider = List<Byte?>::toTypedArray,
    parseValueAction = NavArgByteType::parseValue
)

object NavArgPrimitiveByteArrayType : PrimitiveArrayNavArgType<ByteArray, Byte>(
    emptyArrayProvider = ::byteArrayOf,
    arrayProvider = List<Byte>::toByteArray,
    parseValueAction = ParseUtil::parseByte,
    arrayToStringTransformation = ByteArray::joinToString
)


// Short
object NavArgShortType : SingleNavArgType<Short>(ParseUtil::parseShort)

object NavArgShortListType : ListNavArgType<Short>(NavArgShortType::parseValue)

object NavArgShortSetType : SetNavArgType<Short>(NavArgShortType::parseValue)

object NavArgShortArrayType : ArrayNavArgType<Short>(
    emptyArrayProvider = ::emptyArray,
    arrayProvider = List<Short?>::toTypedArray,
    parseValueAction = NavArgShortType::parseValue
)

object NavArgPrimitiveShortArrayType : PrimitiveArrayNavArgType<ShortArray, Short>(
    emptyArrayProvider = ::shortArrayOf,
    arrayProvider = List<Short>::toShortArray,
    parseValueAction = ParseUtil::parseShort,
    arrayToStringTransformation = ShortArray::joinToString
)


// Int
object NavArgIntType : SingleNavArgType<Int>(IntType::parseValue)

object NavArgIntListType : ListNavArgType<Int>(NavArgIntType::parseValue)

object NavArgIntSetType : SetNavArgType<Int>(NavArgIntType::parseValue)

object NavArgIntArrayType : ArrayNavArgType<Int>(
    emptyArrayProvider = ::emptyArray,
    arrayProvider = List<Int?>::toTypedArray,
    parseValueAction = NavArgIntType::parseValue
)

object NavArgPrimitiveIntArrayType : PrimitiveArrayNavArgType<IntArray, Int>(
    emptyArrayProvider = ::intArrayOf,
    arrayProvider = List<Int>::toIntArray,
    parseValueAction = IntType::parseValue,
    arrayToStringTransformation = IntArray::joinToString
)


// Long
object NavArgLongType : SingleNavArgType<Long>(LongType::parseValue)

object NavArgLongListType : ListNavArgType<Long>(NavArgLongType::parseValue)

object NavArgLongSetType : SetNavArgType<Long>(NavArgLongType::parseValue)

object NavArgLongArrayType : ArrayNavArgType<Long>(
    emptyArrayProvider = ::emptyArray,
    arrayProvider = List<Long?>::toTypedArray,
    parseValueAction = NavArgLongType::parseValue
)

object NavArgPrimitiveLongArrayType : PrimitiveArrayNavArgType<LongArray, Long>(
    emptyArrayProvider = ::longArrayOf,
    arrayProvider = List<Long>::toLongArray,
    parseValueAction = LongType::parseValue,
    arrayToStringTransformation = LongArray::joinToString
)


// Float
object NavArgFloatType : SingleNavArgType<Float>(FloatType::parseValue)

object NavArgFloatListType : ListNavArgType<Float>(NavArgFloatType::parseValue)

object NavArgFloatSetType : SetNavArgType<Float>(NavArgFloatType::parseValue)

object NavArgFloatArrayType : ArrayNavArgType<Float>(
    emptyArrayProvider = ::emptyArray,
    arrayProvider = List<Float?>::toTypedArray,
    parseValueAction = NavArgFloatType::parseValue
)

object NavArgPrimitiveFloatArrayType : PrimitiveArrayNavArgType<FloatArray, Float>(
    emptyArrayProvider = ::floatArrayOf,
    arrayProvider = List<Float>::toFloatArray,
    parseValueAction = FloatType::parseValue,
    arrayToStringTransformation = FloatArray::joinToString
)


// Double
object NavArgDoubleType : SingleNavArgType<Double>(ParseUtil::parseDouble)

object NavArgDoubleListType : ListNavArgType<Double>(NavArgDoubleType::parseValue)

object NavArgDoubleSetType : SetNavArgType<Double>(NavArgDoubleType::parseValue)

object NavArgDoubleArrayType : ArrayNavArgType<Double>(
    emptyArrayProvider = ::emptyArray,
    arrayProvider = List<Double?>::toTypedArray,
    parseValueAction = NavArgDoubleType::parseValue
)

object NavArgPrimitiveDoubleArrayType : PrimitiveArrayNavArgType<DoubleArray, Double>(
    emptyArrayProvider = ::doubleArrayOf,
    arrayProvider = List<Double>::toDoubleArray,
    parseValueAction = ParseUtil::parseDouble,
    arrayToStringTransformation = DoubleArray::joinToString
)


// Enum
class NavArgEnumType<T : Enum<T>>(enumClass: KClass<T>) : SingleNavArgType<T>(enumClass::findValueOf, Enum<T>::name)

class NavArgEnumListType<T : Enum<T>>(enumClass: KClass<T>) : ListNavArgType<T>(enumClass::findValueOf, Enum<T>::name)

class NavArgEnumSetType<T : Enum<T>>(enumClass: KClass<T>) : SetNavArgType<T>(enumClass::findValueOf, Enum<T>::name)

class NavArgEnumArrayType<T : Enum<T>>(enumClass: KClass<T>) : ArrayNavArgType<T>(enumClass::findValueOf, Enum<T>::name, enumClass)


// Parcelable
//TODO -> Creator beim erstellen der Klasse holen, damit man den nicht öfters immer wieder holen muss
class NavArgParcelableType<T : Parcelable>(creator: Parcelable.Creator<T>) : SingleNavArgType<T>(creator::unmarshall, Parcelable::marshall) {
    constructor(clazz: KClass<T>) : this(clazz.CREATOR)
}

class NavArgParcelableListType<T : Parcelable>(creator: Parcelable.Creator<T>) : ListNavArgType<T>(creator::unmarshall, Parcelable::marshall) {
    constructor(clazz: KClass<T>) : this(clazz.CREATOR)
}

class NavArgParcelableSetType<T : Parcelable>(creator: Parcelable.Creator<T>) : SetNavArgType<T>(creator::unmarshall, Parcelable::marshall) {
    constructor(clazz: KClass<T>) : this(clazz.CREATOR)
}

class NavArgParcelableArrayType<T : Parcelable>(clazz: KClass<T>, creator: Parcelable.Creator<T> = clazz.CREATOR) : ArrayNavArgType<T>(
    clazz = clazz,
    parseValueAction = creator::unmarshall,
    serializeValueTransformation = Parcelable::marshall
)



// Serializable
object NavArgSerializableType : SingleNavArgType<Serializable>(Base64Util::deserialize, Serializable::serialize)



//TODO -> Vllt nochmal anschauen
// KotlinSerializable
class NavArgKotlinSerializableType <T: Any>(private val serializer: KSerializer<T>): SingleNavArgType<T>(serializer::deserialize, serializer::serialize) {

    constructor(clazz: KClass<T>): this(clazz.ktxSerializer)

    override fun get(bundle: Bundle, key: String): T? = bundle.getString(key)?.let(serializer::deserialize)

    override fun get(navBackStackEntry: NavBackStackEntry, key: String): T? = navBackStackEntry.get<String>(key)?.let(serializer::deserialize)

    override fun get(savedStateHandle: SavedStateHandle, key: String): T? = savedStateHandle.get<String>(key)?.let(serializer::deserialize)

    override fun put(bundle: Bundle, key: String, value: T?) {
        value?.let {
            bundle.putString(key, serializer.serialize(it))
        }
    }
}