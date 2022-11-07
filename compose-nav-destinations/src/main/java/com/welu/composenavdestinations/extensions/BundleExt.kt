package com.welu.composenavdestinations.extensions

import android.os.Binder
import android.os.Bundle
import android.os.Parcelable
import android.util.Size
import java.io.Serializable

@Suppress("UNCHECKED_CAST", "DEPRECATION")
internal fun <T> Bundle.getTyped(key: String): T? = get(key) as T?

internal fun <T : Any> Bundle.put(key: String, value: T?) {
    @Suppress("UNCHECKED_CAST")
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
        is Array<*> -> when {
            value.isArrayOf<String>() -> putStringArray(key, value as Array<String>)
            value.isArrayOf<CharSequence>() -> putCharSequenceArray(key, value as Array<CharSequence>)
            value.isArrayOf<Parcelable>() -> putParcelableArray(key, value as Array<Parcelable>)
            else -> putSerializable(key, value)
        }
        is Parcelable -> putParcelable(key, value)
        is Serializable -> putSerializable(key, value)
        is Size -> putSize(key, value)
        is Binder -> putBinder(key, value)
        is List<*> -> throw IllegalArgumentException("List must implement Serializable or Parcelable")
        is Set<*> -> throw IllegalArgumentException("Set must implement Serializable or Parcelable")
        else -> throw IllegalArgumentException("Unknown ValueType: ${value::class.simpleName}")
    }
}