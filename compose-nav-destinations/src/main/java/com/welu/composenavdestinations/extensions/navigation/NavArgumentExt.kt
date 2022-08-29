package com.welu.composenavdestinations.extensions.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType

fun <T> navArgument(
    name: String,
    type: NavType<T>,
    defaultValue: Any?,
    nullable: Boolean = false
): NamedNavArgument = androidx.navigation.navArgument(name) {
    this.type = type
    this.defaultValue = defaultValue
    this.nullable = nullable
}

fun <T> navArgument(
    name: String,
    type: NavType<T>,
    nullable: Boolean = false
): NamedNavArgument = androidx.navigation.navArgument(name) {
    this.type = type
    this.nullable = nullable
}