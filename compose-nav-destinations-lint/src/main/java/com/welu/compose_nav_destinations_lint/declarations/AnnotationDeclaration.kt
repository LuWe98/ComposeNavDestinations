package com.welu.compose_nav_destinations_lint.declarations

internal enum class AnnotationDeclaration(override val packageDir: String = "com.welu.composenavdestinations.annotations"): Declaration {
    ComposeDestination,
    ComposeNavGraph;
}