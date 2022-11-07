package com.welu.composenavdestinations.extensions.navigation

import androidx.navigation.NavBackStackEntry

@Suppress("UNCHECKED_CAST", "DEPRECATION")
internal operator fun <T> NavBackStackEntry.get(key: String): T? = arguments?.get(key) as T?