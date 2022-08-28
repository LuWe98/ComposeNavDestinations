package com.welu.composenavdestinations.extensions

import android.annotation.SuppressLint
import android.os.BaseBundle
import android.os.Binder
import android.os.Bundle
import android.os.Parcelable
import android.util.ArrayMap
import android.util.Size
import java.io.Serializable

@SuppressLint("DiscouragedPrivateApi")
private val mapField = lazy {
    BaseBundle::class.java.getDeclaredField("mMap").apply {
        isAccessible = true
    }
}

@Suppress("UNCHECKED_CAST")
internal fun <T> Bundle.getTyped(key: String): T? = get(key) as T?

//@Suppress("UNCHECKED_CAST")
//fun <T: Any> Bundle.put(key: String, value: T?) {
//    (mapField.value[this] as ArrayMap<String, Any>)[key] = value
//}

@Suppress("UNCHECKED_CAST")
fun <T : Any> Bundle.put(key: String, value: T?) {
    when (value) {
        null -> return
        is Boolean -> putBoolean(key, value)
        is BooleanArray -> putBooleanArray(key, value)
        is Short -> putShort(key, value)
        is ShortArray -> putShortArray(key, value)
        is Int -> putInt(key, value)
        is IntArray -> putIntArray(key, value)
        is Long -> putLong(key, value)
        is LongArray -> putLongArray(key, value)
        is Float -> putFloat(key, value)
        is FloatArray -> putFloatArray(key, value)
        is Double -> putDouble(key, value)
        is DoubleArray -> putDoubleArray(key, value)
        is Char -> putChar(key, value)
        is CharArray -> putCharArray(key, value)
        is Byte -> putByte(key, value)
        is ByteArray -> putByteArray(key, value)
        is String -> putString(key, value)
        is Parcelable -> putParcelable(key, value)
        is Serializable -> putSerializable(key, value)
        is Size -> putSize(key, value)
        is Binder -> putBinder(key, value)
        is Array<*> -> when {
            value.isArrayOf<String>() -> putStringArray(key, value as Array<String>)
            value.isArrayOf<CharSequence>() -> putCharSequenceArray(key, value as Array<CharSequence>)
            value.isArrayOf<Parcelable>() -> putParcelableArray(key, value as Array<Parcelable>)
            else -> throw IllegalArgumentException("Unknown Array ValueType ${value::class.simpleName}")
        }
        //TODO -> Noch implementieren für Parcelable - is SparseArray<*>
        is List<*> -> throw IllegalArgumentException("List must implement Serializable or Parcelable")
        is Set<*> -> throw IllegalArgumentException("Set must implement Serializable or Parcelable")
        else -> throw IllegalArgumentException("Unknown ValueType: ${value::class.simpleName}")
    }
}