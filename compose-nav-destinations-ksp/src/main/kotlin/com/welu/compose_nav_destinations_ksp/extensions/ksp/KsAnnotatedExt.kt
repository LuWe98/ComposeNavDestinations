package com.welu.compose_nav_destinations_ksp.extensions.ksp

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.welu.compose_nav_destinations_ksp.annotations.AnnotationDeclaration

fun KSAnnotated.getAnnotationWith(annotationName: String): KSAnnotation? = annotations.firstOrNull { it.shortName.asString() == annotationName }

fun KSAnnotated.getAnnotationWith(annotationDeclaration: com.welu.compose_nav_destinations_ksp.annotations.AnnotationDeclaration): KSAnnotation? = getAnnotationWith(annotationDeclaration.name)

fun KSAnnotated.requireAnnotationWith(annotationName: String): KSAnnotation = getAnnotationWith(annotationName) ?: throw IllegalStateException()

fun KSAnnotated.requireAnnotationWith(annotationDeclaration: com.welu.compose_nav_destinations_ksp.annotations.AnnotationDeclaration): KSAnnotation = getAnnotationWith(annotationDeclaration) ?: throw IllegalStateException()

inline fun <reified T> KSAnnotated.getAnnotationArgument(annotation: com.welu.compose_nav_destinations_ksp.annotations.AnnotationDeclaration, argName: String): T = getAnnotationArgument(annotation.name, argName)

inline fun <reified T> KSAnnotated.getAnnotationArgument(annotationName: String, argName: String): T = requireAnnotationWith(annotationName).requireValueArgument(argName).typedValue()