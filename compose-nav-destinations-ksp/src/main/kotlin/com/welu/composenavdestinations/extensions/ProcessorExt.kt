package com.welu.composenavdestinations.extensions

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.welu.composenavdestinations.annotaioninfo.NavDestinationAnnotation
import com.welu.composenavdestinations.utils.Constants

fun Resolver.getNavDestinations(): Sequence<KSClassDeclaration> = getSymbolsWithAnnotation(NavDestinationAnnotation.identifier)
    .filterIsInstance<KSClassDeclaration>()


val Resolver.dependencies get() = Dependencies(false, *getAllFiles().toList().toTypedArray())