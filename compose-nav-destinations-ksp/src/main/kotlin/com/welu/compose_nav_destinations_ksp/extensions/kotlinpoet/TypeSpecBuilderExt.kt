package com.welu.compose_nav_destinations_ksp.extensions.kotlinpoet

import com.squareup.kotlinpoet.TypeSpec

fun TypeSpec.Companion.buildClass(name: String, action: TypeSpec.Builder.() -> Unit): TypeSpec {
    val builder = classBuilder(name)
    builder.apply(action)
    return builder.build()
}

fun TypeSpec.Companion.buildObject(name: String, action: TypeSpec.Builder.() -> Unit): TypeSpec {
    val builder = objectBuilder(name)
    builder.apply(action)
    return builder.build()
}