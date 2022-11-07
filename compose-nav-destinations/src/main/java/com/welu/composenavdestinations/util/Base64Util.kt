package com.welu.composenavdestinations.util

import android.util.Base64
import com.welu.composenavdestinations.util.Base64Util.BASE_64_CODING_FLAGS
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream
import java.io.Serializable

internal object Base64Util {
    const val BASE_64_CODING_FLAGS = Base64.URL_SAFE or Base64.NO_WRAP

    @Suppress("UNCHECKED_CAST")
    fun <T : Serializable> deserialize(base64String: String): T {
        ObjectInputStream(ByteArrayInputStream(base64String.fromBase64ToByteArray())).use { inputStream ->
            return inputStream.readObject() as T
        }
    }
}

//TODO -> Wenn was nicht mehr gehen sollte mit serializable, dann das hier anpassen
//toByteArray(Charset.defaultCharset())
internal fun String.fromBase64ToByteArray(): ByteArray = Base64.decode(this, BASE_64_CODING_FLAGS)

internal fun ByteArray.toBase64String(): String = Base64.encodeToString(this, BASE_64_CODING_FLAGS)

internal fun String.toBase64(): String = Base64.encodeToString(toByteArray(), BASE_64_CODING_FLAGS)

internal fun String.fromBase64(): String = String(Base64.decode(this, BASE_64_CODING_FLAGS))


//val String.asBase64 get(): String = Base64.encodeToString(toByteArray(), BASE_64_CODING_FLAGS)
//
//val String.fromBase64 get(): String = String(fromBase64ToByteArray)

//internal val String.asEncodedBase64 get(): String = Uri.encode(toBase64())
//
//internal val String.fromBase64 get(): String = String(Base64.decode(this, BASE_64_CODING_FLAGS))