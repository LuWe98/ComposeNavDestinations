package com.welu.composenavdestinations.util

import android.net.Uri
import android.util.Base64
import com.welu.composenavdestinations.util.Base64Util.BASE_64_CODING_FLAGS
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream
import java.io.Serializable
import java.nio.charset.Charset

object Base64Util {
    val BASE_64_CODING_FLAGS = Base64.URL_SAFE or Base64.NO_WRAP

    @Suppress("UNCHECKED_CAST")
    fun <T : Serializable> deserialize(base64String: String): T {
        //Base64.decode(base64String, BASE_64_CODING_FLAGS)
        val decoded = base64String.fromBase64ToByteArray
        ObjectInputStream(ByteArrayInputStream(decoded)).use { inputStream ->
            return inputStream.readObject() as T
        }
    }
}

val String.fromBase64ToByteArray get(): ByteArray = Base64.decode(toByteArray(Charset.defaultCharset()), BASE_64_CODING_FLAGS)

val ByteArray.asBase64String get(): String = Base64.encodeToString(this, BASE_64_CODING_FLAGS)

//val String.asBase64 get(): String = Base64.encodeToString(toByteArray(), BASE_64_CODING_FLAGS)
//
//val String.fromBase64 get(): String = String(fromBase64ToByteArray)


val String.asBase64 get(): String = Base64.encodeToString(toByteArray(), BASE_64_CODING_FLAGS)

val String.asEncodedBase64 get(): String = Uri.encode(asBase64)

val String.fromBase64 get(): String = String(Base64.decode(this, BASE_64_CODING_FLAGS))