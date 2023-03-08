package com.welu.composenavdestinations.extensions.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument as jetpackNavArgument

fun <T> navArgument(
    name: String,
    type: NavType<T>,
    defaultValue: T?,
    nullable: Boolean = false
): NamedNavArgument = jetpackNavArgument(name) {
    this.type = type
    this.defaultValue = defaultValue
    this.nullable = nullable
}

@JvmName("navArgumentUntypedDefaultValue")
fun <T> navArgument(
    name: String,
    type: NavType<T>,
    defaultValue: Any?,
    nullable: Boolean = false
): NamedNavArgument = jetpackNavArgument(name) {
    this.type = type
    this.defaultValue = defaultValue
    this.nullable = nullable
}

fun <T> navArgument(
    name: String,
    type: NavType<T>,
    nullable: Boolean = false
): NamedNavArgument = jetpackNavArgument(name) {
    this.type = type
    this.nullable = nullable
}