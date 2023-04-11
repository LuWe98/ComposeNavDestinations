package com.welu.composenavdestinations.result

import android.os.Parcelable
import java.io.Serializable

/**
 * Marker Interface for DestinationResults
 */
sealed interface DestinationResult<out T> {
    val value: T
    val key: String
}

class BooleanResult(
    override val value: Boolean,
    override val key: String = Boolean::class.qualifiedName!!
): DestinationResult<Boolean>

class BooleanArrayResult(
    override val value: BooleanArray,
    override val key: String = BooleanArray::class.qualifiedName!!
): DestinationResult<BooleanArray>

class StringResult(
    override val value: String,
    override val key: String = String::class.qualifiedName!!
): DestinationResult<String>

class StringArrayResult(
    override val value: Array<String>,
    override val key: String = Array<String>::class.qualifiedName!!
): DestinationResult<Array<String>>

class CharResult(
    override val value: Char,
    override val key: String = Char::class.qualifiedName!!
): DestinationResult<Char>

class CharArrayResult(
    override val value: CharArray,
    override val key: String = CharArray::class.qualifiedName!!
): DestinationResult<CharArray>

class ShortResult(
    override val value: Short,
    override val key: String = Short::class.qualifiedName!!
): DestinationResult<Short>

class ShortArrayResult(
    override val value: ShortArray,
    override val key: String = ShortArray::class.qualifiedName!!
): DestinationResult<ShortArray>

class IntResult(
    override val value: Int,
    override val key: String = Integer::class.qualifiedName!!
): DestinationResult<Int>

class IntArrayResult(
    override val value: IntArray,
    override val key: String = IntArray::class.qualifiedName!!
): DestinationResult<IntArray>

class LongResult(
    override val value: Long,
    override val key: String = Long::class.qualifiedName!!
): DestinationResult<Long>

class LongArrayResult(
    override val value: LongArray,
    override val key: String = LongArray::class.qualifiedName!!
): DestinationResult<LongArray>

class FloatResult(
    override val value: Float,
    override val key: String = Float::class.qualifiedName!!
): DestinationResult<Float>

class FloatArrayResult(
    override val value: FloatArray,
    override val key: String = FloatArray::class.qualifiedName!!
): DestinationResult<FloatArray>

class DoubleResult(
    override val value: Double,
    override val key: String = Double::class.qualifiedName!!
): DestinationResult<Double>

class DoubleArrayResult(
    override val value: DoubleArray,
    override val key: String = DoubleArray::class.qualifiedName!!
): DestinationResult<DoubleArray>

class SerializableResult<T: Serializable>(
    override val value: T,
    override val key: String = value::class.qualifiedName!!
): DestinationResult<T>

class ParcelableResult<T: Parcelable>(
    override val value: T,
    override val key: String = value::class.qualifiedName!!
): DestinationResult<T>

class ParcelableArrayResult<T: Parcelable>(
    override val value: Array<T>,
    override val key: String = value::class.qualifiedName!!
): DestinationResult<Array<T>>

class ParcelableListResult<T: Parcelable>(
    override val value: ArrayList<T>,
    override val key: String = value::class.qualifiedName!!
): DestinationResult<ArrayList<T>>


/**
 * This should only be used, when a generic solution is needed and if it is possible to add this [value] to a Bundle.
 */
class UntypedResult(
    override val value: Any,
    override val key: String = value::class.qualifiedName!!
): DestinationResult<Any>