package com.welu.compose_nav_destinations_lint.declarations

internal sealed interface Declaration {
    val name: String
    val packageDir: String
    val qualifiedName: String get() = "$packageDir.$name"
}