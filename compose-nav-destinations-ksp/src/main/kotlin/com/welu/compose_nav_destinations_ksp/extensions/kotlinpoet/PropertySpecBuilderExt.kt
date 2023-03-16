package com.welu.compose_nav_destinations_ksp.extensions.kotlinpoet

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName

fun PropertySpec.Companion.build(
    name: String,
    typeName: TypeName,
    vararg modifiers: KModifier,
    action: PropertySpec.Builder.() -> Unit
): PropertySpec {
    val builder = builder(name, typeName)
    builder.addModifiers(*modifiers)
    builder.apply(action)
    return builder.build()
}

fun PropertySpec.Builder.getter(action: FunSpec.Builder.() -> Unit) {
    val builder = FunSpec.getterBuilder()
    builder.apply(action)
    getter(builder.build())
}