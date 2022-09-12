package com.welu.composenavdestinations.extensions.ksp

import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeArgument
import com.welu.composenavdestinations.exceptions.TypeResolveException

fun KSTypeArgument.toResolvedType(): KSType? {
    val resolvedType = type?.resolve()
    if (resolvedType?.isError == true) throw TypeResolveException("Resolved type contains errors: $resolvedType")
    return resolvedType
}