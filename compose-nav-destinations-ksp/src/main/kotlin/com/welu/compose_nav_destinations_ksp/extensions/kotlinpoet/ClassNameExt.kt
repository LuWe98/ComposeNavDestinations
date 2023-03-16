package com.welu.compose_nav_destinations_ksp.extensions.kotlinpoet

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.welu.compose_nav_destinations_ksp.extensions.toClassName
import com.welu.compose_nav_destinations_ksp.model.ImportInfo

internal fun ClassName.parameterizedBy(
    vararg imports: ImportInfo
): ParameterizedTypeName = parameterizedBy(*imports.map { it.toClassName() }.toTypedArray())
