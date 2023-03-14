package com.welu.compose_nav_destinations_ksp.extensions.kotlinpoet

import com.squareup.kotlinpoet.FunSpec

fun FunSpec.Companion.build(name: String, action: FunSpec.Builder.() -> Unit): FunSpec {
    val builder = FunSpec.builder(name)
    builder.apply(action)
    return builder.build()
}