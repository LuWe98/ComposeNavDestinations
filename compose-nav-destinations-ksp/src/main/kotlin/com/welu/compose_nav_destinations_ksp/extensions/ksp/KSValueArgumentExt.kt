package com.welu.compose_nav_destinations_ksp.extensions.ksp

import com.google.devtools.ksp.symbol.KSValueArgument

inline fun <reified T> KSValueArgument.typedValue() = value as T
