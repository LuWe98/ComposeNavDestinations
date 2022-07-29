package com.welu.composenavdestinations.extensions

import androidx.lifecycle.SavedStateHandle

inline fun <reified T> SavedStateHandle.requireArg(key: String): T = get<T>(key)!!