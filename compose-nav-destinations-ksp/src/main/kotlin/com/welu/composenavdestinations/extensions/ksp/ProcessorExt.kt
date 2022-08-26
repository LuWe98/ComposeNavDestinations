package com.welu.composenavdestinations.extensions.ksp

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.welu.composenavdestinations.annotationinfo.NavArgumentAnnotation
import com.welu.composenavdestinations.annotationinfo.NavDestinationAnnotation

fun Resolver.getNavDestinations(): Sequence<KSFunctionDeclaration> = getSymbolsWithAnnotation(NavDestinationAnnotation.identifier)
    .filterIsInstance<KSFunctionDeclaration>()

fun Resolver.getNavArguments(): Sequence<KSValueParameter> = getSymbolsWithAnnotation(NavArgumentAnnotation.identifier)
    .filterIsInstance<KSValueParameter>()

fun Resolver.getComposables(): Sequence<KSFunctionDeclaration> = getSymbolsWithAnnotation("androidx.compose.runtime.Composable")
    .filterIsInstance<KSFunctionDeclaration>()

val Resolver.dependencies get() = Dependencies(false, *getAllFiles().toList().toTypedArray())