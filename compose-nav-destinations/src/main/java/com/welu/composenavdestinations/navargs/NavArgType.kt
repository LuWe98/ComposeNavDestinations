package com.welu.composenavdestinations.navargs

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import com.welu.composenavdestinations.extensions.get
import com.welu.composenavdestinations.extensions.getTyped
import com.welu.composenavdestinations.extensions.put

sealed class NavArgType<T: Any?>: NavType<T>(true) {

    abstract fun serializeValue(value: T): String?

    override fun get(bundle: Bundle, key: String): T? = bundle.getTyped(key)

    override fun put(bundle: Bundle, key: String, value: T) = bundle.put(key, value)

    open fun get(navBackStackEntry: NavBackStackEntry, key: String): T? = navBackStackEntry[key]

    open fun get(savedStateHandle: SavedStateHandle, key: String): T? = savedStateHandle[key]

}