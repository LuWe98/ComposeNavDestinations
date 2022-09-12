package com.welu.composenavdestinations.extensions.ksp

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.welu.composenavdestinations.annotations.NavDestinationDefinitionAnnotation
import com.welu.composenavdestinations.annotations.NavGraphDefinitionAnnotation

//fun Resolver.getNavDestinations(): Sequence<KSFunctionDeclaration> = getSymbolsWithAnnotation(NavDestinationAnnotation.import.qualifiedName)
//    .filterIsInstance<KSFunctionDeclaration>()
//
//fun Resolver.getNavArguments(): Sequence<KSValueParameter> = getSymbolsWithAnnotation(NavArgumentAnnotation.import.qualifiedName)
//    .filterIsInstance<KSValueParameter>()

val Resolver.dependencies get() = Dependencies(false, *getAllFiles().toList().toTypedArray())

inline fun <reified NodeType: KSNode> Resolver.findAnnotatedNodesWith(annotationQualifiedName: String): Sequence<NodeType> =
    getSymbolsWithAnnotation(annotationQualifiedName).filterIsInstance<NodeType>()

fun Resolver.getNavDestinationDefinitions() = findAnnotatedNodesWith<KSClassDeclaration>(NavDestinationDefinitionAnnotation.import.qualifiedName)

fun Resolver.getNavGraphDefinitions() = findAnnotatedNodesWith<KSClassDeclaration>(NavGraphDefinitionAnnotation.import.qualifiedName)

fun Resolver.findAnnotatedClassesWith(annotationQualifiedName: String) = findAnnotatedNodesWith<KSClassDeclaration>(annotationQualifiedName)