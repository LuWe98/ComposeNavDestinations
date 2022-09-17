package com.welu.composenavdestinations.extensions.ksp

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.welu.composenavdestinations.annotations.NavDestinationDefinitionAnnotation
import com.welu.composenavdestinations.annotations.NavGraphDefaultDefinitionAnnotation
import com.welu.composenavdestinations.annotations.NavGraphDefinitionAnnotation
import com.welu.composenavdestinations.model.ImportInfo

//fun Resolver.getNavDestinations(): Sequence<KSFunctionDeclaration> = getSymbolsWithAnnotation(NavDestinationAnnotation.import.qualifiedName)
//    .filterIsInstance<KSFunctionDeclaration>()
//
//fun Resolver.getNavArguments(): Sequence<KSValueParameter> = getSymbolsWithAnnotation(NavArgumentAnnotation.import.qualifiedName)
//    .filterIsInstance<KSValueParameter>()

val Resolver.dependencies get() = Dependencies(false, *getAllFiles().toList().toTypedArray())

fun Resolver.findAnnotatedNodesWith(annotationQualifiedName: String): Sequence<KSNode> = getSymbolsWithAnnotation(annotationQualifiedName)

inline fun <reified NodeType: KSNode> Resolver.findAnnotatedNodesTyped(annotationQualifiedName: String): Sequence<NodeType> = findAnnotatedNodesWith(annotationQualifiedName).filterIsInstance<NodeType>()

inline fun <reified NodeType: KSNode> Resolver.findAnnotatedNodesTyped(importInfo: ImportInfo): Sequence<NodeType> = findAnnotatedNodesTyped(importInfo.qualifiedName)


fun Resolver.findAnnotatedClassesWith(annotationQualifiedName: String) = findAnnotatedNodesTyped<KSClassDeclaration>(annotationQualifiedName)

fun Resolver.findNavDestinationDefinitions() = findAnnotatedNodesTyped<KSClassDeclaration>(NavDestinationDefinitionAnnotation.import)

fun Resolver.findNavGraphDefinitions() = findAnnotatedNodesTyped<KSClassDeclaration>(NavGraphDefinitionAnnotation.import)

fun Resolver.getDefinitionsWithDefaultNavGraphAnnotation() = findAnnotatedNodesTyped<KSClassDeclaration>(NavGraphDefaultDefinitionAnnotation.import)