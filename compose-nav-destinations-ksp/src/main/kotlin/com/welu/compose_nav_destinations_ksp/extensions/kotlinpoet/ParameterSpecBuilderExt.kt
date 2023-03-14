package com.welu.compose_nav_destinations_ksp.extensions.kotlinpoet

import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeName

fun ParameterSpec.Companion.build(name: String, typeName: TypeName, action: ParameterSpec.Builder.() -> Unit): ParameterSpec {
    val builder = builder(name, typeName)
    builder.apply(action)
    return builder.build()
}