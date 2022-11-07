package com.welu.composenavdestinations.navargs

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import com.welu.composenavdestinations.extensions.navigation.get
import com.welu.composenavdestinations.extensions.getTyped
import com.welu.composenavdestinations.extensions.put

sealed class NavArgType<T: Any?>: NavType<T>(true) {

    abstract fun serializeValue(value: T): String?

    override fun put(bundle: Bundle, key: String, value: T) = bundle.put(key, value)

    override fun get(bundle: Bundle, key: String): T? = bundle.getTyped(key)

    open fun get(navBackStackEntry: NavBackStackEntry, key: String): T? = navBackStackEntry[key]

    open fun get(savedStateHandle: SavedStateHandle, key: String): T? = savedStateHandle[key]

    /**
     * Returns the NavArgument as a typed parameter from a [NavBackStackEntry].
     * This ensures that compiler Errors will not occur (Array<String> would not be compatible with Array<String?>?).
     * Should not be called otherwise.
     */
    @Suppress("UNCHECKED_CAST")
    fun <R> getTyped(navBackStackEntry: NavBackStackEntry, key: String): R? = get(navBackStackEntry, key) as R?

    /**
     * Returns the NavArgument as a typed parameter from a [SavedStateHandle].
     * This ensures that compiler Errors will not occur (Array<String> would not be compatible with Array<String?>?).
     * Should not be called otherwise.
     */
    @Suppress("UNCHECKED_CAST")
    fun <R> getTyped(savedStateHandle: SavedStateHandle, key: String): R? = get(savedStateHandle, key) as R?

}