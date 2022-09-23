package com.welu.composenavdestinations.extensions.navigation

import androidx.navigation.NavBackStackEntry

internal inline fun <reified T> NavBackStackEntry.requireArg(key: String): T = get(key)!!

@Suppress("UNCHECKED_CAST")
internal operator fun <T> NavBackStackEntry.get(key: String): T? = arguments?.get(key) as T?