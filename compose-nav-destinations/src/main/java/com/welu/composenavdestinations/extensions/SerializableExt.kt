package com.welu.composenavdestinations.extensions

import com.welu.composenavdestinations.util.toBase64String
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream
import java.io.Serializable

internal fun <T: Serializable> T.serialize(): String {
    ByteArrayOutputStream().use { byteStream ->
        ObjectOutputStream(byteStream).use { objectStream ->
            objectStream.writeObject(this)
            objectStream.flush()
        }
        return byteStream.toByteArray().toBase64String()
    }
}