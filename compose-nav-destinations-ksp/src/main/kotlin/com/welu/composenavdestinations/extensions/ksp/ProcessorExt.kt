package com.welu.composenavdestinations.extensions.ksp

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.welu.composenavdestinations.annotations.NavArgumentAnnotation
import com.welu.composenavdestinations.annotations.NavDestinationAnnotation
import com.welu.composenavdestinations.annotations.NavDestinationDefinitionAnnotation
import com.welu.composenavdestinations.annotations.NavGraphDefinitionAnnotation

//fun Resolver.getNavDestinations(): Sequence<KSFunctionDeclaration> = getSymbolsWithAnnotation(NavDestinationAnnotation.import.qualifiedName)
//    .filterIsInstance<KSFunctionDeclaration>()
//
//fun Resolver.getNavArguments(): Sequence<KSValueParameter> = getSymbolsWithAnnotation(NavArgumentAnnotation.import.qualifiedName)
//    .filterIsInstance<KSValueParameter>()

val Resolver.dependencies get() = Dependencies(false, *getAllFiles().toList().toTypedArray())

fun Resolver.getNavDestinationDefinitions() : Sequence<KSClassDeclaration> = getSymbolsWithAnnotation(NavDestinationDefinitionAnnotation.import.qualifiedName)
    .filterIsInstance<KSClassDeclaration>()

fun Resolver.getNavGraphDefinitions(): Sequence<KSClassDeclaration> = getSymbolsWithAnnotation(NavGraphDefinitionAnnotation.import.qualifiedName)
    .filterIsInstance<KSClassDeclaration>()

fun Resolver.getNavDestinationDefinitionsOfNavGraph(graphQualifiedName: String): Sequence<KSClassDeclaration> = getSymbolsWithAnnotation(graphQualifiedName)
    .filterIsInstance<KSClassDeclaration>()