package com.welu.composenavdestinations.extensions

import com.welu.composenavdestinations.util.asBase64String
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream
import java.io.Serializable

internal fun <T: Serializable> T.serialize(): String {
    ByteArrayOutputStream().use { byteStream ->
        ObjectOutputStream(byteStream).use {
            it.writeObject(this)
            it.flush()
        }
        //Base64.encodeToString(byteStream.toByteArray(), Base64Util.BASE_64_CODING_FLAGS)
        return byteStream.toByteArray().asBase64String
    }
}