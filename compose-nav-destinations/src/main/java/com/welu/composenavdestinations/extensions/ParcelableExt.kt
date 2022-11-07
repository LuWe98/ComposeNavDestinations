package com.welu.composenavdestinations.extensions

import android.os.BadParcelableException
import android.os.Parcel
import android.os.Parcelable
import com.welu.composenavdestinations.util.toBase64String
import com.welu.composenavdestinations.util.fromBase64ToByteArray
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
internal val <T : Parcelable> KClass<T>.CREATOR
    get() : Parcelable.Creator<T> = try {
        java.getField("CREATOR").get(null) as Parcelable.Creator<T>
    } catch (e: Exception) {
        throw BadParcelableException(e)
    } catch (t: Throwable) {
        throw BadParcelableException(t.message)
    }

internal fun Parcelable.marshall(): String = Parcel.obtain().let { parcel ->
    writeToParcel(parcel, 0)
    parcel.marshall().toBase64String().also {
        parcel.recycle()
    }
}

internal fun <T: Parcelable> Parcelable.Creator<T>.unmarshall(value: String): T = Parcel.obtain().let { parcel ->
    parcel.unmarshall(value.fromBase64ToByteArray())
    parcel.setDataPosition(0)
    createFromParcel(parcel).also {
        parcel.recycle()
    }
}

internal fun Parcel.unmarshall(arr: ByteArray) = unmarshall(arr, 0, arr.size)