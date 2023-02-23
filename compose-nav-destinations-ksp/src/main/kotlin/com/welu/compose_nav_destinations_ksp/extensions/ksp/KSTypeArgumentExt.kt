package com.welu.compose_nav_destinations_ksp.extensions.ksp

import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeArgument
import com.welu.compose_nav_destinations_ksp.exceptions.TypeResolveException

fun KSTypeArgument.toResolvedType(): KSType? {
    val resolvedType = type?.resolve()
    if (resolvedType?.isError == true) throw com.welu.compose_nav_destinations_ksp.exceptions.TypeResolveException("Resolved type contains errors: $resolvedType")
    return resolvedType
}