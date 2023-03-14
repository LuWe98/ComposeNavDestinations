package com.welu.compose_nav_destinations_ksp.extensions.kotlinpoet

import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName

fun PropertySpec.Companion.build(name: String, typeName: TypeName, action: PropertySpec.Builder.() -> Unit): PropertySpec {
    val builder = builder(name, typeName)
    builder.apply(action)
    return builder.build()
}