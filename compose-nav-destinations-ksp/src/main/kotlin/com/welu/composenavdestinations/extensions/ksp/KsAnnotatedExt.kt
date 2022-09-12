package com.welu.composenavdestinations.extensions.ksp

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.welu.composenavdestinations.annotations.AnnotationDeclaration

fun KSAnnotated.getAnnotationWith(annotationName: String): KSAnnotation? = annotations.firstOrNull { it.shortName.asString() == annotationName }

fun KSAnnotated.getAnnotationWith(annotationDeclaration: AnnotationDeclaration): KSAnnotation? = getAnnotationWith(annotationDeclaration.name)

fun KSAnnotated.requireAnnotationWith(annotationName: String): KSAnnotation = getAnnotationWith(annotationName) ?: throw IllegalStateException()

fun KSAnnotated.requireAnnotationWith(annotationDeclaration: AnnotationDeclaration): KSAnnotation = getAnnotationWith(annotationDeclaration) ?: throw IllegalStateException()

inline fun <reified T> KSAnnotated.getAnnotationArgument(argName: String, annotation: AnnotationDeclaration): T =
    requireAnnotationWith(annotation).requireValueArgument(argName).typedValue()