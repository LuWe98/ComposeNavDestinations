package com.welu.composenavdestinations.extensions.ksp

import com.google.devtools.ksp.symbol.KSValueArgument

inline fun <reified T> KSValueArgument.typedValue() = value as T
