package com.welu.composenavdestinations.navargs

import android.net.Uri

object NavArgConstants {

    const val ENCODED_NULL_VALUE = "%@_NULL_@"
    val DECODED_NULL_VALUE: String = Uri.decode(ENCODED_NULL_VALUE)

    const val ENCODED_EMPTY_STRING = "%@_EMPTY_S_@"
    val DECODED_EMPTY_STRING: String = Uri.decode(ENCODED_EMPTY_STRING)

//    const val ENCODED_EMPTY_CHAR = "%@_EMPTY_C_@"
//    val DECODED_EMPTY_CHAR: String = Uri.decode(ENCODED_EMPTY_CHAR)

    const val ENCODED_VALUE_SEPARATOR = "%@_SEPARATOR_@"
    val DECODED_VALUE_SEPARATOR: String = Uri.decode(ENCODED_VALUE_SEPARATOR)

}