package com.welu.composenavdestinations.extensions

import androidx.compose.ui.text.toLowerCase
import com.welu.composenavdestinations.tt.Bar
import java.util.*

fun Bar.test() : String = toString()

fun String.toLowerCaseTest() = lowercase(Locale.getDefault())