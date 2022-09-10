package com.welu.composenavdestinations.extensions.ksp

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.welu.composenavdestinations.annotations.NavArgumentAnnotation
import com.welu.composenavdestinations.annotations.NavDestinationAnnotation

fun Resolver.getNavDestinations(): Sequence<KSFunctionDeclaration> = getSymbolsWithAnnotation(NavDestinationAnnotation.import.qualifiedName)
    .filterIsInstance<KSFunctionDeclaration>()

fun Resolver.getNavArguments(): Sequence<KSValueParameter> = getSymbolsWithAnnotation(NavArgumentAnnotation.import.qualifiedName)
    .filterIsInstance<KSValueParameter>()

fun Resolver.getComposables(): Sequence<KSFunctionDeclaration> = getSymbolsWithAnnotation("androidx.compose.runtime.Composable")
    .filterIsInstance<KSFunctionDeclaration>()

val Resolver.dependencies get() = Dependencies(false, *getAllFiles().toList().toTypedArray())


fun Resolver.getNavDestinationDefinitions() : Sequence<KSClassDeclaration> = getSymbolsWithAnnotation("com.welu.composenavdestinations.spec.tests.NavDestinationDefinition")
    .filterIsInstance<KSClassDeclaration>()