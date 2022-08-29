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

    override fun put(bundle: Bundle, key: String, value: T) = bundle.put(key, value)

    override fun get(bundle: Bundle, key: String): T? = bundle.getTyped(key)

    fun get(navBackStackEntry: NavBackStackEntry, key: String): T? = navBackStackEntry[key]

    fun get(savedStateHandle: SavedStateHandle, key: String): T? = savedStateHandle[key]

    //Returns the NavArgument as a typed Parameter. This ensures that compiler Errors will not occur -> Array<String> would not be compatible with Array<String?>?
    //Shouldnt be called otherwise
    fun <R> getTyped(navBackStackEntry: NavBackStackEntry, key: String): R? = navBackStackEntry[key]

    fun <R> getTyped(savedStateHandle: SavedStateHandle, key: String): R? = savedStateHandle[key]

}