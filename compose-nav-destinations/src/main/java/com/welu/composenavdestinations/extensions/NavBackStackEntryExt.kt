package com.welu.composenavdestinations.extensions

import androidx.navigation.NavBackStackEntry

inline fun <reified T> NavBackStackEntry.requireArg(key: String): T = arguments!!.get(key) as T